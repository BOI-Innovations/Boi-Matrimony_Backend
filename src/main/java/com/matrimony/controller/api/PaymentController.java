package com.matrimony.controller.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.matrimony.model.entity.RazorpayOrder;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.repository.RazorpayOrderRepository;
import com.matrimony.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private RazorpayOrderRepository razorpayOrderRepository;

	@PostMapping("/create-order")
	public ResponseEntity createOrder(@RequestParam String planId) {
		return paymentService.createOrder(planId);
	}

	@PostMapping("/verify-payment")
	public ResponseEntity verifyPayment(@RequestParam String razorpayOrderId, @RequestParam String razorpayPaymentId,
			@RequestParam String razorpaySignature, @RequestParam String method) {
		return paymentService.capturePayment(razorpayOrderId, razorpayPaymentId, razorpaySignature, method);
	}
	
	@PostMapping("/generate-qr")
	public ResponseEntity generateQr(@RequestParam String orderId) {
	    return paymentService.generateQr(orderId);
	}
	
	@GetMapping("/check-status")
	public ResponseEntity checkStatus(@RequestParam String orderId) {
	    RazorpayOrder order = razorpayOrderRepository.findByRazorpayOrderId(orderId)
	        .orElseThrow(() -> new RuntimeException("Order not found"));

	    Map<String, Object> payload = new HashMap<>();
	    payload.put("status", order.getStatus().name());
	    return new ResponseEntity("Order status fetched", 200, payload);
	}
	
}
