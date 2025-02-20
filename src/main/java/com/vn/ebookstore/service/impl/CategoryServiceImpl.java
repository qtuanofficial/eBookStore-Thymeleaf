package com.vn.ebookstore.service.impl;

import com.vn.ebookstore.model.Category;
import com.vn.ebookstore.repository.CategoryRepository;
import com.vn.ebookstore.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}