package com.vn.ebookstore.repository;

import com.vn.ebookstore.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findByUserIdOrderByCreatedAtDesc(Integer userId);
    List<OrderDetail> findByStatus(String status);
    
    @Query(value = """
        SELECT DATE_FORMAT(o.created_at, '%Y-%m') as date, 
               SUM(o.total) as amount 
        FROM orders o 
        WHERE o.status = 'DELIVERED'
        GROUP BY DATE_FORMAT(o.created_at, '%Y-%m')
        ORDER BY date DESC
        LIMIT 12
        """, nativeQuery = true)
    List<Object[]> getRevenueByMonth();

    @Query(value = """
        SELECT b.id, b.title, b.author, 
               COUNT(oi.id) as quantity,
               SUM(oi.price * oi.quantity) as revenue
        FROM books b
        JOIN order_items oi ON b.id = oi.book_id
        JOIN orders o ON oi.order_id = o.id
        WHERE o.status = 'DELIVERED'
        GROUP BY b.id
        ORDER BY quantity DESC
        LIMIT :limit
        """, nativeQuery = true)
    List<Object[]> getTopSellingBooks(@Param("limit") int limit);

    @Query(value = """
        SELECT c.name, COUNT(oi.id) as count
        FROM categories c
        JOIN books b ON b.category_id = c.id
        JOIN order_items oi ON oi.book_id = b.id
        JOIN orders o ON o.id = oi.order_id
        WHERE o.status = 'DELIVERED'
        GROUP BY c.id
        """, nativeQuery = true)
    List<Object[]> getSalesByCategory();

    @Query(value = """
        SELECT status, COUNT(*) as count
        FROM orders
        GROUP BY status
        """, nativeQuery = true)
    List<Object[]> getOrderCountByStatus();

    @Query(value = """
        SELECT p.provider, COUNT(*) as count
        FROM orders o
        JOIN payment_details p ON p.order_id = o.id
        GROUP BY p.provider
        """, nativeQuery = true)
    List<Object[]> getOrderCountByPaymentMethod();
}