package com.vn.ebookstore.controller.admin;

import com.vn.ebookstore.model.Book;
import com.vn.ebookstore.service.BookService;
import com.vn.ebookstore.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import jakarta.annotation.PostConstruct;

@Controller
@RequestMapping("/admin/books")
public class AdminBookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private CategoryService categoryService;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";
    private static final String COVER_DIR = UPLOAD_DIR + "covers/";
    private static final String EBOOK_DIR = UPLOAD_DIR + "ebooks/";

    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("pageTitle", "Quản lý sách");
        return "page/admin/book/index";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("pageTitle", "Thêm sách mới");
        return "page/admin/book/create";
    }

    @PostMapping("/create")
    public String createBook(@ModelAttribute Book book,
                           @RequestParam("coverFile") MultipartFile coverFile,
                           @RequestParam(value = "bookFile", required = false) MultipartFile bookFile,
                           RedirectAttributes redirectAttributes) {
        try {
            // Handle cover image upload
            if (!coverFile.isEmpty()) {
                String coverFileName = UUID.randomUUID().toString() + getFileExtension(coverFile.getOriginalFilename());
                Path coverPath = Paths.get(COVER_DIR + coverFileName);
                Files.copy(coverFile.getInputStream(), coverPath, StandardCopyOption.REPLACE_EXISTING);
                book.setCover("/uploads/covers/" + coverFileName);
            }

            // Handle ebook file upload
            if (bookFile != null && !bookFile.isEmpty()) {
                String bookFileName = UUID.randomUUID().toString() + getFileExtension(bookFile.getOriginalFilename());
                Path bookPath = Paths.get(EBOOK_DIR + bookFileName);
                Files.copy(bookFile.getInputStream(), bookPath, StandardCopyOption.REPLACE_EXISTING);
                book.getBookDetail().setFileUrl("/uploads/ebooks/" + bookFileName);
            }

            bookService.createBook(book);
            redirectAttributes.addFlashAttribute("success", "Thêm sách thành công");
            return "redirect:/admin/books";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/books/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("pageTitle", "Chỉnh sửa sách");
        return "page/admin/book/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable Integer id, 
                           @ModelAttribute Book book,
                           @RequestParam(value = "coverFile", required = false) MultipartFile coverFile,
                           @RequestParam(value = "bookFile", required = false) MultipartFile bookFile,
                           RedirectAttributes redirectAttributes) {
        try {
            Book existingBook = bookService.getBookById(id);

            // Handle cover image upload
            if (coverFile != null && !coverFile.isEmpty()) {
                // Delete old cover if exists
                if (existingBook.getCover() != null) {
                    deleteFile("src/main/resources/static" + existingBook.getCover());
                }

                String coverFileName = UUID.randomUUID().toString() + getFileExtension(coverFile.getOriginalFilename());
                Path coverPath = Paths.get(COVER_DIR + coverFileName);
                Files.copy(coverFile.getInputStream(), coverPath, StandardCopyOption.REPLACE_EXISTING);
                book.setCover("/uploads/covers/" + coverFileName);
            } else {
                book.setCover(existingBook.getCover());
            }

            // Handle ebook file upload
            if (bookFile != null && !bookFile.isEmpty()) {
                // Delete old file if exists
                if (existingBook.getBookDetail() != null && existingBook.getBookDetail().getFileUrl() != null) {
                    deleteFile("src/main/resources/static" + existingBook.getBookDetail().getFileUrl());
                }

                String bookFileName = UUID.randomUUID().toString() + getFileExtension(bookFile.getOriginalFilename());
                Path bookPath = Paths.get(EBOOK_DIR + bookFileName);
                Files.copy(bookFile.getInputStream(), bookPath, StandardCopyOption.REPLACE_EXISTING);
                book.getBookDetail().setFileUrl("/uploads/ebooks/" + bookFileName);
            } else if (existingBook.getBookDetail() != null) {
                book.getBookDetail().setFileUrl(existingBook.getBookDetail().getFileUrl());
            }

            bookService.updateBook(id, book);
            redirectAttributes.addFlashAttribute("success", "Cập nhật sách thành công");
            return "redirect:/admin/books";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/books/edit/" + id;
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Book book = bookService.getBookById(id);
            
            // Delete cover file
            if (book.getCover() != null) {
                deleteFile("src/main/resources/static" + book.getCover());
            }
            
            // Delete ebook file
            if (book.getBookDetail() != null && book.getBookDetail().getFileUrl() != null) {
                deleteFile("src/main/resources/static" + book.getBookDetail().getFileUrl());
            }

            bookService.softDeleteBook(id);
            redirectAttributes.addFlashAttribute("success", "Xóa sách thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/books";
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf("."));
    }

    private void deleteFile(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            // Log error but don't throw
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void init() {
        // Create upload directories if they don't exist
        try {
            Files.createDirectories(Paths.get(COVER_DIR));
            Files.createDirectories(Paths.get(EBOOK_DIR));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directories!", e);
        }
    }
}
