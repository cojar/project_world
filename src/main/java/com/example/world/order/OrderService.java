package com.example.world.order;

import com.example.world.product.Product;
import com.example.world.user.SiteUser;
import jakarta.persistence.criteria.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    public void create(OrderForm orderForm,Product product, SiteUser user) {
        ProductOrder order = new ProductOrder();
        order.setProduct(product);
        order.setUser(user);
        order.setCustomerName(orderForm.getCustomerName());
        order.setEmail(orderForm.getEmail());
        order.setPayment(orderForm.getPayment());
        order.setOrderDate(LocalDateTime.now());
        this.orderRepository.save(order);
    }

    public ProductOrder getOrder(Long id) {
        Optional<ProductOrder> productOrder = this.orderRepository.findById(id);
        if (productOrder.isPresent()) {
            return productOrder.get();
        } else {
            throw new IllegalArgumentException("주문 정보를 찾을 수 없습니다. id: " + id);
        }
    }

    public void cancleOrder(Long orderId) {
        ProductOrder order = getOrder(orderId);
        order.setOrderStatus("취소요청");
        this.orderRepository.save(order);
    }

    public void delete(ProductOrder order) {
        this.orderRepository.delete(order);
    }
}