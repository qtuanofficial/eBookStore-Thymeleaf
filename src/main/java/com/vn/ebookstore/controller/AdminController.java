package com.vn.ebookstore.controller;

import com.vn.ebookstore.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        // Tổng số sách, đơn hàng, người dùng
        model.addAttribute("totalBooks", bookRepository.count());
        model.addAttribute("totalOrders", orderDetailRepository.count());
        model.addAttribute("totalUsers", userRepository.count());

        // Tổng số danh mục và sub_category
        model.addAttribute("totalCategories", categoryRepository.count());
        model.addAttribute("totalSubCategories", categoryRepository.countSubCategories());

        // Tổng số đơn hàng và người dùng trong 30 ngày qua
        model.addAttribute("recentOrders", orderDetailRepository.countRecentOrders());
        model.addAttribute("recentUsers", userRepository.countRecentUsers());

        return "page/admin/dashboard";
    }
}
