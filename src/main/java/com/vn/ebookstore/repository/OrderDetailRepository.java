package com.vn.ebookstore.repository;

import com.vn.ebookstore.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    @Query("SELECT COUNT(o) FROM OrderDetail o WHERE o.createdAt >= CURRENT_DATE - 30")
    long countRecentOrders();

}