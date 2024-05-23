package com.ngocnguyen.jewelry_ecommerce.service.implement;

import com.ngocnguyen.jewelry_ecommerce.component.CustomUserDetails;
import com.ngocnguyen.jewelry_ecommerce.dto.CartDTO;
import com.ngocnguyen.jewelry_ecommerce.dto.CartItemDTO;
import com.ngocnguyen.jewelry_ecommerce.entity.Order;
import com.ngocnguyen.jewelry_ecommerce.entity.OrderItems;
import com.ngocnguyen.jewelry_ecommerce.entity.Product;
import com.ngocnguyen.jewelry_ecommerce.entity.User;
import com.ngocnguyen.jewelry_ecommerce.repository.OrderItemRepository;
import com.ngocnguyen.jewelry_ecommerce.repository.OrderRepository;
import com.ngocnguyen.jewelry_ecommerce.repository.UserRepository;
import com.ngocnguyen.jewelry_ecommerce.service.CartService;
import com.ngocnguyen.jewelry_ecommerce.service.OrderService;
import com.ngocnguyen.jewelry_ecommerce.service.ProductService;
import com.ngocnguyen.jewelry_ecommerce.utils.CommonConstants;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;

    private User getCurrentUser() throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails currentUserDetail = (CustomUserDetails) authentication.getPrincipal();
            User currentUser = userRepository.findById(currentUserDetail.getUserId()).get();
            return currentUser;
        } else {
            throw new Exception("Chưa đăng nhập");
        }
    }
    @Override
    @Transactional
    public Order createOrder(Order form) throws Exception {
        Order newOrder = preview();
        newOrder.setReceiverAddress(form.getReceiverAddress());
        newOrder.setReceiverName(form.getReceiverName());
        newOrder.setReceiverPhone(form.getReceiverPhone());
        newOrder.setNote(form.getNote());
        newOrder.setStatus(CommonConstants.WAIT_STATUS);
        newOrder.setOrderTime(LocalDateTime.now());
        orderRepository.save(newOrder);

        List<CartItemDTO> listCartItems = cartService.getAllCartItems();
        for (CartItemDTO item : listCartItems){
            OrderItems orderItem = new OrderItems();
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setProductPrice(item.getProduct().getDiscount());
            orderItem.setOrder(newOrder);

            Product product = item.getProduct();
            productService.updateSales(product.getId(), item.getQuantity());
            productService.updateQuantity(product.getId(), -item.getQuantity());
            orderItemRepository.save(orderItem);
        }
        cartService.deleteAll();
        return newOrder;
    }


    @Override
    public void cancelOrder(Long id) throws Exception {
        Optional<Order> order = orderRepository.findById(id);
        List<OrderItems> items = orderItemRepository.findAllByOrder_id(id);
        for(OrderItems item : items){
            Product product = item.getProduct();
            productService.updateQuantity(product.getId(), item.getQuantity());
            productService.updateSales(product.getId(), -item.getQuantity());
        }
        if(order.isPresent()){
            order.get().setStatus(CommonConstants.CANCEL_STATUS);
            orderRepository.save(order.get());
        }
        order.orElseThrow();
    }

    @Override
    public void confirmOrder(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if(order.isPresent()){
            order.get().setStatus(CommonConstants.SUCCESS_STATUS);
            orderRepository.save(order.get());
        }
        order.orElseThrow();
    }

    @Override
    public List<OrderItems> getItemsInOrder(Long id) {
        List<OrderItems> orderItemsList = orderItemRepository.findAllByOrder_id(id);
        return orderItemsList;
    }

    @Override
    public List<Order> getAllOrders() throws Exception {
        return orderRepository.findAllByUser_id(getCurrentUser().getId());
    }

    @Override
    public Optional<Order> getOrder(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order preview() throws Exception {
        Order newOrder = new Order();
        CartDTO cart = cartService.getCart();
        if(cart != null && cart.count() > 0){
            newOrder.setTotalPrice(cart.getCartTotalPrice());
            User currentUser = getCurrentUser();
            newOrder.setUser(currentUser);
            newOrder.setShipping(CommonConstants.SHIPPING);
            newOrder.setStatus(CommonConstants.WAIT_STATUS);
            newOrder.setOrderTime(LocalDateTime.now());
            newOrder.setReceiverName(currentUser.getFirstName() + " " + currentUser.getLastName());
            newOrder.setReceiverPhone(currentUser.getPhone());
            newOrder.setReceiverAddress(currentUser.getAddress());
            return newOrder;
        } else {
            throw new Exception("'Giỏ hàng không có sản phẩm'");
        }
    }

    @Override
    public List<Order> getDeliveringOrder() throws Exception {
        List<Order> delivering = orderRepository.findAllByUser_idAndStatus(getCurrentUser().getId(), CommonConstants.DELIVERING_STATUS);
        List<Order> sorted = delivering.stream()
                        .sorted(Comparator.comparing(Order::getOrderTime, Comparator.nullsLast(Comparator.reverseOrder()))).toList();
        return sorted;

    }

    @Override
    public List<Order> getWaitingOrder() throws Exception {
        List<Order> waitConfirm = orderRepository.findAllByUser_idAndStatus(getCurrentUser().getId(), CommonConstants.WAIT_STATUS);
        List<Order> waitCancel = orderRepository.findAllByUser_idAndStatus(getCurrentUser().getId(), CommonConstants.WAIT_CANCEL);
        waitConfirm.addAll(waitCancel);
        List<Order> sorted = waitConfirm.stream()
                .sorted(Comparator.comparing(Order::getOrderTime, Comparator.nullsLast(Comparator.reverseOrder()))).toList();
        return sorted;
    }

    @Override
    public List<Order> getSuccessOrder() {
        return orderRepository.findAllByStatus(CommonConstants.SUCCESS_STATUS);
    }

    @Override
    public List<Order> getCancelOrder() {
        return orderRepository.findAllByStatus(CommonConstants.CANCEL_STATUS);
    }

    @Override
    public List<Order> history() throws Exception {
        List<Order> success = orderRepository.findAllByUser_idAndStatus(getCurrentUser().getId(), CommonConstants.SUCCESS_STATUS);
        List<Order> cancel = orderRepository.findAllByUser_idAndStatus(getCurrentUser().getId(), CommonConstants.CANCEL_STATUS);
        success.addAll(cancel);
        List<Order> sorted = success.stream()
                .sorted(Comparator.comparing(Order::getOrderTime, Comparator.nullsLast(Comparator.reverseOrder()))).toList();
        return sorted;

    }

    @Override
    public Order save(Order order){
        return orderRepository.save(order);
    }
    @Override
    public void requestCancel(Long id, String reason){
        Optional<Order> order = orderRepository.findById(id);
        if(order.isPresent()){
            order.get().setStatus(CommonConstants.WAIT_CANCEL);
            order.get().setNote(reason);
            orderRepository.save(order.get());
        }
        order.orElseThrow();
    }
    @Override
    public void confirmOrderRequest(Long id){
        Optional<Order> order = orderRepository.findById(id);
        if(order.isPresent()){
            order.get().setStatus(CommonConstants.DELIVERING_STATUS);
            orderRepository.save(order.get());

        }
        order.orElseThrow();
    }
    @Override
    public List<Order> getWaitConfirm(){

        return orderRepository.findAllByStatus(CommonConstants.WAIT_STATUS);
    }
    @Override
    public List<Order> getCancelRequest(){
        return orderRepository.findAllByStatus(CommonConstants.WAIT_CANCEL);
    }
    public int countItem(List<Order> orders){
        int count = 0;
        for (Order order: orders) {
            count += order.getItems().size();
        }
        return count;
    }
    private List<Order> findOrdersByDay(String status, LocalDate date){
        return orderRepository.findAllByStatusAndOrderTimeBetween(status, date.atStartOfDay(), date.plusDays(1).atStartOfDay());
    }
    @Override
    public int[] saleByDay(LocalDate start, LocalDate end){
        ArrayList<Integer> sale = new ArrayList<>();

        LocalDate current = start;
        while (!current.isAfter(end)){
            sale.add(countItem(findOrdersByDay(CommonConstants.SUCCESS_STATUS, current)));
            current = current.plusDays(1);
        }
        int[] result = new int[sale.size()];
        for (int i = 0; i < sale.size(); i++) {
            result[i] = sale.get(i);
        }

        return result;
    }
    @Override
    public String[] arrDay(LocalDate start, LocalDate end){
        ArrayList<String> dates = new ArrayList<>();
        LocalDate current = start;
        while (!current.isAfter(end)){
            dates.add(current.toString());
            current = current.plusDays(1);
        }
        String[] result = new String[dates.size()];
        for (int i = 0; i < dates.size(); i++) {
            result[i] = dates.get(i);
        }

        return result;
    }
}
