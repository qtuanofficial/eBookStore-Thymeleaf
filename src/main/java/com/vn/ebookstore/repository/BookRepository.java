package com.vn.ebookstore.repository;

import com.vn.ebookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findBySubCategory_Id(Integer subCategoryId);
}