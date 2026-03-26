package com.matrimony.service;

import com.matrimony.model.entity.ResponseEntity;

public interface PaymentService {

	ResponseEntity createOrder(String planId);

	ResponseEntity capturePayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature,
			String method);

	ResponseEntity generateQr(String orderId);
}
