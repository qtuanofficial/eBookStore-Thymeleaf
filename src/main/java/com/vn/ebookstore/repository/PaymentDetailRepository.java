package com.vn.ebookstore.repository;

import com.vn.ebookstore.model.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Integer> {
    List<PaymentDetail> findByOrderId(Integer orderId);
    List<PaymentDetail> findByStatus(String status);
    List<PaymentDetail> findByProviderAndStatus(String provider, String status);
}