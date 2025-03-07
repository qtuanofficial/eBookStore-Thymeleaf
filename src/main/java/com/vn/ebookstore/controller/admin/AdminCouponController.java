package com.vn.ebookstore.controller.admin;

import com.vn.ebookstore.model.Coupon;
import com.vn.ebookstore.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Date;

@Controller
@RequestMapping("/admin/coupons")
public class AdminCouponController {

    @Autowired
    private CouponService couponService;

    @GetMapping
    public String listCoupons(Model model) {
        model.addAttribute("coupons", couponService.getAllCoupons());
        model.addAttribute("pageTitle", "Quản lý mã giảm giá");
        return "page/admin/coupon/index";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("coupon", new Coupon());
        model.addAttribute("pageTitle", "Thêm mã giảm giá mới");
        return "page/admin/coupon/create";
    }

    @PostMapping("/create")
    public String createCoupon(@ModelAttribute Coupon coupon,
                             RedirectAttributes redirectAttributes) {
        try {
            coupon.setCode(coupon.getCode().toUpperCase());
            coupon.setCreatedAt(new Date());
            coupon.setTimesUsed(0);
            coupon.setIsActive(true);
            couponService.createCoupon(coupon);
            redirectAttributes.addFlashAttribute("success", "Thêm mã giảm giá thành công");
            return "redirect:/admin/coupons";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/coupons/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Coupon coupon = couponService.getCouponById(id);
        model.addAttribute("coupon", coupon);
        model.addAttribute("pageTitle", "Chỉnh sửa mã giảm giá");
        return "page/admin/coupon/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateCoupon(@PathVariable Integer id,
                             @ModelAttribute Coupon coupon,
                             RedirectAttributes redirectAttributes) {
        try {
            coupon.setId(id);
            coupon.setCode(coupon.getCode().toUpperCase());
            couponService.updateCoupon(coupon);
            redirectAttributes.addFlashAttribute("success", "Cập nhật mã giảm giá thành công");
            return "redirect:/admin/coupons";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/coupons/edit/" + id;
        }
    }

    @PostMapping("/{id}/toggle-status")
    public String toggleCouponStatus(@PathVariable Integer id,
                                   RedirectAttributes redirectAttributes) {
        try {
            Coupon coupon = couponService.getCouponById(id);
            coupon.setIsActive(!coupon.getIsActive());
            couponService.updateCoupon(coupon);
            String status = coupon.getIsActive() ? "kích hoạt" : "vô hiệu hóa";
            redirectAttributes.addFlashAttribute("success", "Đã " + status + " mã giảm giá");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/coupons";
    }
}
