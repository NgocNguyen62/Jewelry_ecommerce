package com.ngocnguyen.jewelry_ecommerce.service;

import com.ngocnguyen.jewelry_ecommerce.entity.Order;
import com.ngocnguyen.jewelry_ecommerce.entity.OrderItems;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order createOrder(Order form) throws Exception;
    void cancelOrder(Long id) throws Exception;
    void confirmOrder(Long id);
    List<OrderItems> getItemsInOrder(Long id);
    List<Order> getAllOrders() throws Exception;
    Optional<Order> getOrder(Long id);
    Order preview() throws Exception;

    List<Order> getDeliveringOrder() throws Exception;

    List<Order> getWaitingOrder() throws Exception;

    List<Order> getSuccessOrder();
    List<Order> getCancelOrder();
    List<Order> history() throws Exception;

    Order save(Order order);

    void requestCancel(Long id, String reason);

    void confirmOrderRequest(Long id);

    List<Order> getWaitConfirm();

    List<Order> getCancelRequest();
}
