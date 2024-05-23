package com.ngocnguyen.jewelry_ecommerce.repository;

import com.ngocnguyen.jewelry_ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser_id(Long id);
    List<Order> findAllByStatus(String status);
    List<Order> findAllByStatusNot(String status);
    List<Order> findAllByUser_idAndStatusNot(Long id, String status);
    List<Order> findAllByUser_idAndStatus(Long id, String status);
    List<Order> findByUser_IdAndStatusOrStatus(Long id, String status1, String status2);
    List<Order> findAllByStatusAndOrderTimeBetween(String status, LocalDateTime start, LocalDateTime end);
}
