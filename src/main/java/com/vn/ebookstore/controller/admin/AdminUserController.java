package com.vn.ebookstore.controller.admin;

import com.vn.ebookstore.model.User;
import com.vn.ebookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("pageTitle", "Quản lý người dùng");
        return "page/admin/user/index";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("pageTitle", "Thêm người dùng mới");
        return "page/admin/user/create";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            // Mã hóa mật khẩu trước khi lưu
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setCreatedAt(new Date());
            userService.save(user);
            redirectAttributes.addFlashAttribute("success", "Thêm người dùng thành công");
            return "redirect:/admin/users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/users/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Chỉnh sửa người dùng");
        return "page/admin/user/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Integer id, 
                           @ModelAttribute User user,
                           @RequestParam(required = false) String newPassword,
                           RedirectAttributes redirectAttributes) {
        try {
            User existingUser = userService.getUserById(id);
            user.setId(id);
            
            // Chỉ cập nhật mật khẩu nếu có nhập mật khẩu mới
            if (newPassword != null && !newPassword.isEmpty()) {
                user.setPassword(passwordEncoder.encode(newPassword));
            } else {
                user.setPassword(existingUser.getPassword());
            }

            userService.save(user);
            redirectAttributes.addFlashAttribute("success", "Cập nhật người dùng thành công");
            return "redirect:/admin/users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/users/edit/" + id;
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            userService.softDeleteUser(id);
            redirectAttributes.addFlashAttribute("success", "Xóa người dùng thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/toggle-status")
    public String toggleUserStatus(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserById(id);
            user.setIsActive(!user.getIsActive());
            userService.save(user);
            String status = user.getIsActive() ? "kích hoạt" : "vô hiệu hóa";
            redirectAttributes.addFlashAttribute("success", "Đã " + status + " người dùng");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users";
    }
}
