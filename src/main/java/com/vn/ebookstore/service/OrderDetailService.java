package com.vn.ebookstore.service;

import com.vn.ebookstore.model.OrderDetail;
import java.util.List;
import java.util.Map;

public interface OrderDetailService {
    OrderDetail createOrder(Integer userId, Integer addressId, String paymentMethod, String note);
    OrderDetail getOrderById(Integer id);
    List<OrderDetail> getOrdersByUserId(Integer userId);
    void cancelOrder(Integer orderId);
    OrderDetail updateOrderStatus(Integer orderId, String status);
    List<OrderDetail> getAllOrders();
    OrderDetail save(OrderDetail order);
    
    double calculateTotalRevenue();
    long countOrdersByStatus(String status);
    List<Map<String, Object>> getRevenueByMonth();
    List<Map<String, Object>> getTopSellingBooks(int limit);
    List<Map<String, Object>> getSalesByCategory();
    List<Map<String, Object>> getOrderCountByStatus();
    List<Map<String, Object>> getOrderCountByPaymentMethod();
}