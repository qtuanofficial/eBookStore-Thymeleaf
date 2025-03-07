package com.vn.ebookstore.controller.admin;

import com.vn.ebookstore.model.Category;
import com.vn.ebookstore.model.SubCategory;
import com.vn.ebookstore.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.Date;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/categories/";

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("pageTitle", "Quản lý danh mục");
        return "page/admin/category/index";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("pageTitle", "Thêm danh mục mới");
        return "page/admin/category/create";
    }

    @PostMapping("/create")
    public String createCategory(@ModelAttribute Category category,
                               @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                               RedirectAttributes redirectAttributes) {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String fileName = UUID.randomUUID().toString() + 
                                getFileExtension(imageFile.getOriginalFilename());
                Path filePath = Paths.get(UPLOAD_DIR + fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                category.setImage("/uploads/categories/" + fileName);
            }

            category.setCreatedAt(new Date());
            categoryService.createCategory(category);
            redirectAttributes.addFlashAttribute("success", "Thêm danh mục thành công");
            return "redirect:/admin/categories";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/categories/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Category category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        model.addAttribute("pageTitle", "Chỉnh sửa danh mục");
        return "page/admin/category/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateCategory(@PathVariable Integer id, 
                               @ModelAttribute Category category,
                               @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                               RedirectAttributes redirectAttributes) {
        try {
            Category existingCategory = categoryService.getCategoryById(id);

            if (imageFile != null && !imageFile.isEmpty()) {
                // Delete old image if exists
                if (existingCategory.getImage() != null) {
                    deleteFile("src/main/resources/static" + existingCategory.getImage());
                }

                String fileName = UUID.randomUUID().toString() + 
                                getFileExtension(imageFile.getOriginalFilename());
                Path filePath = Paths.get(UPLOAD_DIR + fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                category.setImage("/uploads/categories/" + fileName);
            } else {
                category.setImage(existingCategory.getImage());
            }

            categoryService.updateCategory(id, category);
            redirectAttributes.addFlashAttribute("success", "Cập nhật danh mục thành công");
            return "redirect:/admin/categories";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/categories/edit/" + id;
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("success", "Xóa danh mục thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/categories";
    }

    // Sub-category management
    @GetMapping("/{categoryId}/subcategories/create")
    public String showCreateSubCategoryForm(@PathVariable Integer categoryId, Model model) {
        Category category = categoryService.getCategoryById(categoryId);
        SubCategory subCategory = new SubCategory();
        subCategory.setParent(category);
        
        model.addAttribute("category", category);
        model.addAttribute("subCategory", subCategory);
        model.addAttribute("pageTitle", "Thêm danh mục con");
        return "page/admin/category/subcategory-create";
    }

    @PostMapping("/{categoryId}/subcategories/create")
    public String createSubCategory(@PathVariable Integer categoryId,
                                  @ModelAttribute SubCategory subCategory,
                                  RedirectAttributes redirectAttributes) {
        try {
            Category category = categoryService.getCategoryById(categoryId);
            subCategory.setParent(category);
            subCategory.setCreatedAt(new Date());
            categoryService.createSubCategory(subCategory);
            redirectAttributes.addFlashAttribute("success", "Thêm danh mục con thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/categories";
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf("."));
    }

    private void deleteFile(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
