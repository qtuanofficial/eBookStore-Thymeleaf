package com.vn.ebookstore.controller;

import com.vn.ebookstore.model.Book;
import com.vn.ebookstore.repository.BookRepository;
import com.vn.ebookstore.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/books")
public class AdminBookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("")
    public String listBooks(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        return "/page/admin/books/list";
    }

    @GetMapping("/create")
    public String createBookForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categoryRepository.findAll());
        return "/page/admin/books/form";
    }

    @PostMapping("/create")
    public String createBook(@ModelAttribute Book book) {
        bookRepository.save(book);
        return "redirect:/admin/books";
    }

    @GetMapping("/edit/{id}")
    public String editBookForm(@PathVariable Integer id, Model model) {
        model.addAttribute("book", bookRepository.findById(id).orElseThrow());
        model.addAttribute("categories", categoryRepository.findAll());
        return "/page/admin/books/form";
    }

    @PostMapping("/edit/{id}")
    public String editBook(@PathVariable Integer id, @ModelAttribute Book book) {
        book.setId(id);
        bookRepository.save(book);
        return "redirect:/admin/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Integer id) {
        bookRepository.deleteById(id);
        return "redirect:/admin/books";
    }
}