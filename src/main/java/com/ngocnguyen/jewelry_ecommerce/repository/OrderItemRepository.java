package com.ngocnguyen.jewelry_ecommerce.repository;

import com.ngocnguyen.jewelry_ecommerce.entity.Order;
import com.ngocnguyen.jewelry_ecommerce.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItems, Long> {
    List<OrderItems> findAllByOrder_id(Long orderId);
}
