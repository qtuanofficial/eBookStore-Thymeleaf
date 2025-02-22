package com.vn.ebookstore.repository;

import com.vn.ebookstore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("SELECT COUNT(sc) FROM SubCategory sc WHERE sc.category IS NOT NULL")
    long countSubCategories();

}