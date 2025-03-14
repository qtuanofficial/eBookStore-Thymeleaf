package com.vn.ebookstore.repository;

import com.vn.ebookstore.model.BookDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookDetailRepository extends JpaRepository<BookDetail, Integer> {
}