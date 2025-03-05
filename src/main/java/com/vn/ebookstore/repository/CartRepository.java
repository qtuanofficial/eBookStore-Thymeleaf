package com.vn.ebookstore.repository;

import com.vn.ebookstore.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUserIdAndUpdatedAtIsNull(int userId);
}