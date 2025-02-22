package com.vn.ebookstore.repository;

import com.vn.ebookstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= CURRENT_DATE - 30")
    long countRecentUsers();


}
