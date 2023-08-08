package com.example.world.order;

import com.example.world.DataNotFoundException;
import com.example.world.product.Product;
import com.example.world.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public void create(OrderForm orderForm, Product product, SiteUser user) {
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


    public boolean isOrderValid(Long orderId) {
        Optional<ProductOrder> productOrder = orderRepository.findById(orderId);
        return productOrder.isPresent();
    }

    public ProductOrder getProductOrderByProduct(Product product) {
        return orderRepository.findByProduct(product)
                .orElseThrow(() -> new DataNotFoundException("Product order not found"));
    }

    public List<ProductOrder> getOrderList() {
        return this.orderRepository.findAll();
    }

    public long getCompletedOrderCount() {
        return orderRepository.countByOrderStatus("발송완료");
    }

    public void updateOrderStatus(Long id, String orderStatus) {
        ProductOrder productOrder = getOrder(id);
        productOrder.setOrderStatus(orderStatus);
        this.orderRepository.save(productOrder);
    }

    public void updateOrderSendCode(Long id, String sendCode) {
        ProductOrder productOrder = getOrder(id);
        productOrder.setCode(sendCode);
        productOrder.setOrderStatus("발송완료");
        this.orderRepository.save(productOrder);
    }


    public void cancleOrder(Long id) {
        ProductOrder productOrder = getOrder(id);
        productOrder.setOrderStatus("취소요청");
        this.orderRepository.save(productOrder);
    }

    public void delete(ProductOrder order) {
        this.orderRepository.delete(order);
    }



    public List<ProductOrder> getOrdersBetweenDates(LocalDateTime start, LocalDateTime end) {
        return this.orderRepository.findByOrderDateBetween(start, end);
    }


}