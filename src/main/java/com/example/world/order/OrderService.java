package com.example.world.order;

import jakarta.persistence.criteria.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    public void create(OrderForm orderForm) {
        ProductOrder order = new ProductOrder();
        order.setProduct(orderForm.getProduct());
        order.setUser(orderForm.getUser());
        order.setCustomerName(orderForm.getCustomerName());
        order.setEmail(orderForm.getEmail());
        order.setPayment(orderForm.getPayment());
        order.setOrderStatus(order.getOrderStatus());
        order.setCode(order.getCode());
        order.setOrderDate(LocalDateTime.now());
        this.orderRepository.save(order);
    }

    public void delete(ProductOrder order) {
        this.orderRepository.delete(order);
    }

    public ProductOrder getOrder(Integer id) {
        Optional<ProductOrder> order = this.orderRepository.findById(id);
        return order.get();
    }
}
