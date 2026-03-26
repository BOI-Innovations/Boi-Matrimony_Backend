package com.matrimony.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.matrimony.model.dto.response.CreateOrderResponse;
import com.matrimony.model.entity.RazorpayOrder;
import com.matrimony.model.entity.RazorpayPayment;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.SubscriptionPlan;
import com.matrimony.model.entity.User;
import com.matrimony.model.entity.UserSubscription;
import com.matrimony.model.enums.OrderStatus;
import com.matrimony.model.enums.PaymentStatus;
import com.matrimony.model.enums.SubscriptionStatus;
import com.matrimony.repository.RazorpayOrderRepository;
import com.matrimony.repository.RazorpayPaymentRepository;
import com.matrimony.repository.SubscriptionPlanRepository;
import com.matrimony.repository.UserRepository;
import com.matrimony.repository.UserSubscriptionRepository;
import com.matrimony.service.EmailService;
import com.matrimony.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import com.razorpay.QrCode;
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

	@Value("${razorpay.key}")
	private String key;

	@Value("${razorpay.secret}")
	private String secret;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private RazorpayOrderRepository orderRepository;

	@Autowired
	private RazorpayPaymentRepository paymentRepository;

	@Autowired
	private SubscriptionPlanRepository planRepository;

	@Autowired
	private UserSubscriptionRepository subscriptionRepository;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	EmailService emailService;

//	@Override
//	public ResponseEntity createOrder(String planId) {
//
//		try {
//			String username = SecurityContextHolder.getContext().getAuthentication().getName();
//			Long userId = userRepository.findUserIdByUsername(username);
//
//			SubscriptionPlan plan = planRepository.findById(planId)
//					.orElseThrow(() -> new RuntimeException("Plan not found"));
//
//			RazorpayClient client = new RazorpayClient(key, secret);
//
//			JSONObject notes = new JSONObject();
//			notes.put("user_id", userId);
//			notes.put("plan_id", plan.getPlanId());
//			notes.put("plan_name", plan.getName());
//			notes.put("purchase_type", "subscription");
//			notes.put("app", "matrimonial");
//
//			JSONObject orderRequest = new JSONObject();
//			orderRequest.put("amount", plan.getPrice() * 100);
//			orderRequest.put("currency", plan.getCurrency());
//			orderRequest.put("receipt", "sub_" + userId + "_" + System.currentTimeMillis());
//			orderRequest.put("notes", notes);
//
//			Order razorpayOrder = client.orders.create(orderRequest);
//
//			User userRef = entityManager.getReference(User.class, userId);
//
//			RazorpayOrder order = new RazorpayOrder();
//			order.setUser(userRef);
//			order.setPlan(plan);
//			order.setAmount(BigDecimal.valueOf(plan.getPrice()));
//			order.setCurrency(plan.getCurrency());
//			order.setRazorpayOrderId(razorpayOrder.get("id"));
//			order.setStatus(OrderStatus.CREATED);
//			order.setNotes(notes.toString());
//
//			orderRepository.save(order);
//
//			CreateOrderResponse response = new CreateOrderResponse();
//			response.setOrderId(razorpayOrder.get("id"));
//			response.setAmount(razorpayOrder.get("amount"));
//			response.setCurrency(razorpayOrder.get("currency"));
//			response.setStatus(razorpayOrder.get("status"));
//			response.setReceipt(razorpayOrder.get("receipt"));
//			response.setNotes(convertJsonToMap(razorpayOrder.get("notes")));
//
//			return new ResponseEntity("Order created successfully", 200, response);
//
//		} catch (Exception e) {
//			return new ResponseEntity("Order creation failed", 500, e.getMessage());
//		}
//	}

	@Override
	public com.matrimony.model.entity.ResponseEntity createOrder(String planId) {
		try {
			// 1️⃣ Get logged-in user
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			Long userId = userRepository.findUserIdByUsername(username);

			// 2️⃣ Find plan
			SubscriptionPlan plan = planRepository.findById(planId)
					.orElseThrow(() -> new RuntimeException("Plan not found"));

			// 3️⃣ Check for existing unpaid order
			List<OrderStatus> reusableStatuses = List.of(OrderStatus.CREATED, OrderStatus.FAILED);

			Optional<RazorpayOrder> existingOrderOpt = orderRepository
					.findTopByUser_IdAndPlan_PlanIdAndStatusInOrderByCreatedAtDesc(userId, planId, reusableStatuses);

			if (existingOrderOpt.isPresent()) {
				RazorpayOrder order = existingOrderOpt.get();

				CreateOrderResponse response = new CreateOrderResponse();
				response.setOrderId(order.getRazorpayOrderId());
				response.setAmount(order.getAmount().multiply(BigDecimal.valueOf(100)).longValue()); // paise
				response.setCurrency(order.getCurrency());
				response.setStatus(order.getStatus().name());

				JSONObject notesJson = new JSONObject(order.getNotes() != null ? order.getNotes() : "{}");
				response.setNotes(convertJsonToMap(notesJson));

				return new com.matrimony.model.entity.ResponseEntity("Existing unpaid order found", 200, response);
			}

			// 4️⃣ Create new Razorpay order
			RazorpayClient client = new RazorpayClient(key, secret);

			JSONObject notes = new JSONObject();
			notes.put("user_id", userId);
			notes.put("plan_id", plan.getPlanId());
			notes.put("plan_name", plan.getName());
			notes.put("purchase_type", "subscription");
			notes.put("app", "matrimonial");

			BigDecimal price = plan.getPrice() != null ? BigDecimal.valueOf(plan.getPrice()) : BigDecimal.ZERO;
			long amountInPaise = price.multiply(BigDecimal.valueOf(100)).longValueExact();

			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", amountInPaise);
			orderRequest.put("currency", plan.getCurrency());
			orderRequest.put("receipt", "sub_" + userId + "_" + System.currentTimeMillis());
			orderRequest.put("notes", notes);

			Order razorpayOrder = client.orders.create(orderRequest);

			// 5️⃣ Save order in DB
			User userRef = entityManager.getReference(User.class, userId);

			RazorpayOrder newOrder = new RazorpayOrder();
			newOrder.setUser(userRef);
			newOrder.setPlan(plan);
			newOrder.setAmount(price); // store in rupees
			newOrder.setCurrency(plan.getCurrency());
			newOrder.setRazorpayOrderId(razorpayOrder.get("id"));
			newOrder.setStatus(OrderStatus.CREATED);
			newOrder.setNotes(notes.toString());

			orderRepository.save(newOrder);

			// 6️⃣ Build response safely
			CreateOrderResponse response = new CreateOrderResponse();
			response.setOrderId(razorpayOrder.get("id"));

			// ✅ Safe conversion: handle Integer or Long
			Number amountNumber = (Number) razorpayOrder.get("amount");
			response.setAmount(amountNumber.longValue());

			response.setCurrency(razorpayOrder.get("currency"));
			response.setStatus(razorpayOrder.get("status"));
			response.setReceipt(razorpayOrder.get("receipt"));

			JSONObject razorpayNotesJson = (JSONObject) razorpayOrder.get("notes");
			response.setNotes(convertJsonToMap(razorpayNotesJson));

			return new com.matrimony.model.entity.ResponseEntity("Order created successfully", 200, response);

		} catch (Exception e) {
			e.printStackTrace();
			return new com.matrimony.model.entity.ResponseEntity("Order creation failed", 500, e.getMessage());
		}
	}

	@Override
	public com.matrimony.model.entity.ResponseEntity generateQr(String orderId) {
	    try {

	        RazorpayClient client = new RazorpayClient(key, secret);

	        // Fetch order from DB
	        RazorpayOrder order = orderRepository
	                .findByRazorpayOrderId(orderId)
	                .orElseThrow(() -> new RuntimeException("Order not found"));

	        long amountInPaise = order.getAmount()
	                .multiply(BigDecimal.valueOf(100))
	                .longValueExact();

	        JSONObject qrRequest = new JSONObject();
	        qrRequest.put("type", "upi_qr");
	        qrRequest.put("name", "Community Membership");
	        qrRequest.put("usage", "single_use");
	        qrRequest.put("fixed_amount", true);
	        qrRequest.put("payment_amount", amountInPaise);
	        qrRequest.put("description", "Community Support Membership");

	        JSONObject notes = new JSONObject(order.getNotes());
	        qrRequest.put("notes", notes);

	        com.razorpay.QrCode qrCode = client.qrCode.create(qrRequest);

	        Map<String, Object> response = new HashMap<>();
	        response.put("qrId", qrCode.get("id"));
	        response.put("imageUrl", qrCode.get("image_url"));
	        response.put("status", qrCode.get("status"));

	        return new com.matrimony.model.entity.ResponseEntity(
	                "QR generated successfully",
	                200,
	                response
	        );

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new com.matrimony.model.entity.ResponseEntity(
	                "QR generation failed",
	                500,
	                e.getMessage()
	        );
	    }
	}
//	@Override
//	public ResponseEntity capturePayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature,
//			String method) {
//
//		try {
//			String payload = razorpayOrderId + "|" + razorpayPaymentId;
//
//			boolean isValid = verifySignature(payload, razorpaySignature, secret);
//
//			if (!isValid) {
//				return new ResponseEntity("Invalid payment signature", 400, null);
//			}
//
//			RazorpayOrder order = orderRepository.findByRazorpayOrderId(razorpayOrderId)
//					.orElseThrow(() -> new RuntimeException("Order not found"));
//
//			if (order.getStatus() == OrderStatus.PAID) {
//				return new ResponseEntity("Order already paid", 400, null);
//			}
//
//			RazorpayPayment payment = new RazorpayPayment();
//			payment.setRazorpayPaymentId(razorpayPaymentId);
//			payment.setRazorpayOrderId(razorpayOrderId);
//			payment.setOrder(order);
//			payment.setAmount(order.getAmount());
//			payment.setCurrency(order.getCurrency());
//			payment.setMethod(method);
//			payment.setStatus(PaymentStatus.CAPTURED);
//
//			paymentRepository.save(payment);
//
//			order.setStatus(OrderStatus.PAID);
//			orderRepository.save(order);
//
//			UserSubscription subscription = new UserSubscription();
//			subscription.setUser(order.getUser());
//			subscription.setPlan(order.getPlan());
//			subscription.setStartDate(LocalDate.now());
//			subscription.setEndDate(LocalDate.now().plusDays(order.getPlan().getDurationDays()));
//			subscription.setStatus(SubscriptionStatus.ACTIVE);
//			subscription.setPayment(payment);
//
//			subscriptionRepository.save(subscription);
//
//			return new ResponseEntity("Payment verified & subscription activated", 200, subscription);
//
//		} catch (Exception e) {
//			return new ResponseEntity("Payment processing failed", 500, e.getMessage());
//		}
//	}

	@Override
	public ResponseEntity capturePayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature,
			String method) {

		try {
			String payload = razorpayOrderId + "|" + razorpayPaymentId;
			boolean isValid = verifySignature(payload, razorpaySignature, secret);

			if (!isValid) {
				return new ResponseEntity("Invalid payment signature", 400, null);
			}

			RazorpayOrder order = orderRepository.findByRazorpayOrderId(razorpayOrderId)
					.orElseThrow(() -> new RuntimeException("Order not found"));

			if (order.getStatus() == OrderStatus.PAID) {
				return new ResponseEntity("Order already paid", 400, null);
			}

			// 1️⃣ Save payment
			RazorpayPayment payment = new RazorpayPayment();
			payment.setRazorpayPaymentId(razorpayPaymentId);
			payment.setRazorpayOrderId(razorpayOrderId);
			payment.setOrder(order);
			payment.setAmount(order.getAmount());
			payment.setCurrency(order.getCurrency());
			payment.setMethod(method);
			payment.setStatus(PaymentStatus.CAPTURED);

			paymentRepository.save(payment);

			// 2️⃣ Update order status
			order.setStatus(OrderStatus.PAID);
			orderRepository.save(order);

			// 3️⃣ Subscription handling (extend or create)
			Optional<UserSubscription> existingSubOpt = subscriptionRepository
					.findByUserAndPlanAndStatus(order.getUser(), order.getPlan(), SubscriptionStatus.ACTIVE);

			UserSubscription subscription;

			if (existingSubOpt.isPresent()) {

				subscription = existingSubOpt.get();

				LocalDate baseDate = subscription.getEndDate().isAfter(LocalDate.now()) ? subscription.getEndDate()
						: LocalDate.now();

				subscription.setEndDate(baseDate.plusDays(order.getPlan().getDurationDays()));

				subscription.setPayment(payment);

			} else {

				subscription = new UserSubscription();
				subscription.setUser(order.getUser());
				subscription.setPlan(order.getPlan());
				subscription.setStartDate(LocalDate.now());
				subscription.setEndDate(LocalDate.now().plusDays(order.getPlan().getDurationDays()));
				subscription.setStatus(SubscriptionStatus.ACTIVE);
				subscription.setPayment(payment);
			}

			subscriptionRepository.save(subscription);

			
			User user = order.getUser(); 

			emailService.sendSubscriptionConfirmation(
			        user.getId(),
			        user.getEmail(),
			        order.getPlan().getName()
			);


			return new ResponseEntity("Payment verified & subscription updated successfully", 200, subscription);

		} catch (Exception e) {
			return new ResponseEntity("Payment processing failed", 500, e.getMessage());
		}
	}

	private boolean verifySignature(String payload, String actualSignature, String secret) throws Exception {

		Mac mac = Mac.getInstance("HmacSHA256");
		SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");

		mac.init(keySpec);

		byte[] hash = mac.doFinal(payload.getBytes());

		String generatedSignature = Hex.encodeHexString(hash);

		return generatedSignature.equals(actualSignature);
	}

	private Map<String, Object> convertJsonToMap(JSONObject json) {
		Map<String, Object> map = new HashMap<>();
		if (json != null) {
			for (String key : json.keySet()) {
				map.put(key, json.get(key));
			}
		}
		return map;
	}
}
