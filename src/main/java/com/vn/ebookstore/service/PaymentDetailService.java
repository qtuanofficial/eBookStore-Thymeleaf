package com.vn.ebookstore.service;

import com.vn.ebookstore.model.PaymentDetail;
import java.util.List;

public interface PaymentDetailService {
    PaymentDetail createPayment(Integer orderId, String paymentMethod, Long amount);
    PaymentDetail getPaymentById(Integer id);
    List<PaymentDetail> getPaymentsByOrderId(Integer orderId);
    PaymentDetail updatePaymentStatus(Integer paymentId, String status);
    PaymentDetail save(PaymentDetail payment);
}