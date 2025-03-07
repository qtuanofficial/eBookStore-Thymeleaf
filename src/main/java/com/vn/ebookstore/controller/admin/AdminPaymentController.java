package com.vn.ebookstore.controller.admin;

import com.vn.ebookstore.model.PaymentDetail;
import com.vn.ebookstore.model.OrderDetail;
import com.vn.ebookstore.service.PaymentDetailService;
import com.vn.ebookstore.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;

@Controller 
@RequestMapping("/admin/payments")
public class AdminPaymentController {

    @Autowired
    private PaymentDetailService paymentService;
    
    @Autowired
    private OrderDetailService orderService;

    @GetMapping
    public String listPayments(Model model) {
        model.addAttribute("payments", paymentService.getAllPayments());
        model.addAttribute("pageTitle", "Quản lý thanh toán");
        return "page/admin/payment/index";
    }

    @PostMapping("/{id}/status")
    @Transactional
    public String updatePaymentStatus(@PathVariable Integer id,
                                    @RequestParam String status,
                                    RedirectAttributes redirectAttributes) {
        try {
            PaymentDetail payment = paymentService.getPaymentById(id);
            
            if (payment == null) {
                throw new RuntimeException("Payment not found");
            }

            // Update order status based on payment status
            OrderDetail order = payment.getOrder();
            if (status.equals("SUCCESS")) {
                order.setStatus("CONFIRMED");
                orderService.save(order);
            } else if (status.equals("FAILED")) {
                order.setStatus("CANCELLED");
                orderService.save(order);
            }

            payment.setStatus(status);
            paymentService.save(payment);
            
            redirectAttributes.addFlashAttribute("success", "Payment status updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/payments";
    }
}
