package com.vn.ebookstore.controller.admin;

import com.vn.ebookstore.model.OrderDetail;
import com.vn.ebookstore.model.PaymentDetail;
import com.vn.ebookstore.service.OrderDetailService;
import com.vn.ebookstore.service.PaymentDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    @Autowired
    private OrderDetailService orderService;

    @Autowired
    private PaymentDetailService paymentService;

    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("pageTitle", "Quản lý đơn hàng");
        return "page/admin/order/index";
    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Integer id, Model model) {
        OrderDetail order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        model.addAttribute("pageTitle", "Chi tiết đơn hàng #" + id);
        return "page/admin/order/detail";
    }

    @PostMapping("/{id}/status")
    @Transactional
    public String updateStatus(@PathVariable Integer id, 
                             @RequestParam String status,
                             RedirectAttributes redirectAttributes) {
        try {
            OrderDetail order = orderService.getOrderById(id);
            
            if (order == null) {
                throw new RuntimeException("Order not found");
            }

            // Validate status transition
            if (!isValidStatusTransition(order.getStatus(), status)) {
                throw new RuntimeException("Invalid status transition");
            }

            // Update payment status if needed
            if (status.equals("DELIVERED")) {
                updatePaymentStatus(order, "SUCCESS");
            } else if (status.equals("CANCELLED")) {
                updatePaymentStatus(order, "CANCELLED");
            }

            order = orderService.updateOrderStatus(id, status);
            redirectAttributes.addFlashAttribute("success", 
                "Order status updated successfully");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/orders/" + id;
    }

    private void updatePaymentStatus(OrderDetail order, String status) {
        if (!order.getPayments().isEmpty()) {
            PaymentDetail payment = order.getPayments().get(0);
            payment.setStatus(status);
            paymentService.save(payment);
        }
    }

    private boolean isValidStatusTransition(String currentStatus, String newStatus) {
        // PENDING -> PREPARING/CANCELLED
        // PREPARING -> SHIPPING/CANCELLED
        // SHIPPING -> DELIVERED/CANCELLED
        // DELIVERED/CANCELLED -> No further transitions
        
        switch (currentStatus) {
            case "PENDING":
                return newStatus.equals("PREPARING") || newStatus.equals("CANCELLED");
            case "PREPARING": 
                return newStatus.equals("SHIPPING") || newStatus.equals("CANCELLED");
            case "SHIPPING":
                return newStatus.equals("DELIVERED") || newStatus.equals("CANCELLED");
            case "DELIVERED":
            case "CANCELLED":
                return false; // Không cho phép chuyển đổi từ trạng thái này
            default:
                return false;
        }
    }

    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Integer id,
                            RedirectAttributes redirectAttributes) {
        try {
            orderService.cancelOrder(id);
            redirectAttributes.addFlashAttribute("success", "Đã hủy đơn hàng");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/orders/" + id;
    }
}
