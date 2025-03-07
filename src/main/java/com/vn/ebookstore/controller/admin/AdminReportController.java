package com.vn.ebookstore.controller.admin;

import com.vn.ebookstore.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.text.SimpleDateFormat;

@Controller
@RequestMapping("/admin/reports")
public class AdminReportController {

    @Autowired
    private OrderDetailService orderService;

    @Autowired
    private BookService bookService;

    @GetMapping
    public String showReports(Model model) {
        model.addAttribute("pageTitle", "Báo cáo thống kê");
        
        // Thống kê doanh thu
        model.addAttribute("revenueStats", getRevenueStats());
        
        // Thống kê sản phẩm
        model.addAttribute("productStats", getProductStats());
        
        // Thống kê đơn hàng
        model.addAttribute("orderStats", getOrderStats());
        
        return "page/admin/report/index";
    }

    @GetMapping("/filter")
    public String filterReport(@RequestParam String startDate,
                             @RequestParam String endDate,
                             Model model) {
        try {
            Date start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            Date end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);

            model.addAttribute("revenueStats", getRevenueStatsByDateRange(start, end));
            model.addAttribute("productStats", getProductStatsByDateRange(start, end));
            model.addAttribute("orderStats", getOrderStatsByDateRange(start, end));
            
            return "page/admin/report/index";
        } catch (Exception e) {
            throw new RuntimeException("Invalid date format");
        }
    }

    private Map<String, Object> getRevenueStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Tính tổng doanh thu
        double totalRevenue = orderService.calculateTotalRevenue();
        stats.put("total", totalRevenue);
        
        // Số đơn thành công
        long successOrders = orderService.countOrdersByStatus("DELIVERED");
        stats.put("successOrders", successOrders);
        
        // Trung bình mỗi đơn
        double avgOrderValue = successOrders > 0 ? totalRevenue / successOrders : 0;
        stats.put("avgOrderValue", avgOrderValue);
        
        // Dữ liệu biểu đồ doanh thu theo thời gian
        List<Map<String, Object>> chartData = orderService.getRevenueByMonth();
        stats.put("chartData", chartData);
        
        return stats;
    }

    private Map<String, Object> getProductStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Top sản phẩm bán chạy
        stats.put("topSelling", orderService.getTopSellingBooks(10));
        
        // Phân bố theo danh mục
        stats.put("categoryData", orderService.getSalesByCategory());
        
        return stats;
    }

    private Map<String, Object> getOrderStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Thống kê theo trạng thái đơn hàng
        stats.put("statusData", orderService.getOrderCountByStatus());
        
        // Thống kê theo phương thức thanh toán
        stats.put("paymentData", orderService.getOrderCountByPaymentMethod());
        
        return stats;
    }

    private Map<String, Object> getRevenueStatsByDateRange(Date start, Date end) {
        Map<String, Object> stats = new HashMap<>();
        // Implement date range filtering
        return stats;  
    }

    private Map<String, Object> getProductStatsByDateRange(Date start, Date end) {
        Map<String, Object> stats = new HashMap<>();
        // Implement date range filtering
        return stats;
    }

    private Map<String, Object> getOrderStatsByDateRange(Date start, Date end) {
        Map<String, Object> stats = new HashMap<>();
        // Implement date range filtering
        return stats;
    }
}
