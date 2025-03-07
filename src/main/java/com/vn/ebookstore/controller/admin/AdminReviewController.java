package com.vn.ebookstore.controller.admin;

import com.vn.ebookstore.model.Review;
import com.vn.ebookstore.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/reviews")
public class AdminReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public String listReviews(Model model) {
        model.addAttribute("reviews", reviewService.getAllReviews());
        model.addAttribute("pageTitle", "Quản lý đánh giá");
        return "page/admin/review/index";
    }

    @PostMapping("/{id}/approve")
    public String approveReview(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Review review = reviewService.getReviewById(id);
            review.setStatus("APPROVED");
            reviewService.save(review);
            redirectAttributes.addFlashAttribute("success", "Đã duyệt đánh giá");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/reviews";
    }

    @PostMapping("/{id}/reject")
    public String rejectReview(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Review review = reviewService.getReviewById(id);
            review.setStatus("REJECTED");
            reviewService.save(review);
            redirectAttributes.addFlashAttribute("success", "Đã từ chối đánh giá");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/reviews";
    }

    @PostMapping("/delete/{id}")
    public String deleteReview(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            reviewService.deleteReview(id);
            redirectAttributes.addFlashAttribute("success", "Đã xóa đánh giá");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/reviews";
    }
}
