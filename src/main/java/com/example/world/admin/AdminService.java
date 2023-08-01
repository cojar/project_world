package com.example.world.admin;

import com.example.world.order.OrderRepository;
import com.example.world.order.ProductOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final OrderRepository orderRepository;

    public Page<ProductOrder> getList(int page, int size) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("orderDate"));
        Pageable pageable = PageRequest.of(page, size, Sort.by(sorts));
        return this.adminRepository.findAll(pageable);
    }

    public void updateOrderSendCode(Long id, String sendCode, String orderStatus) {
        ProductOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));
        order.setCode(sendCode);
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
    }

}
