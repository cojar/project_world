package com.example.world.admin;

import com.example.world.order.OrderRepository;
import com.example.world.order.ProductOrder;
import com.example.world.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
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



    public int requestMonthPrice(YearMonth date) {
        LocalDateTime start = LocalDateTime.of(date.getYear(), date.getMonth(), 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(date.atEndOfMonth(), LocalTime.of(23, 59, 59));

        List<ProductOrder> list = this.orderRepository.findByOrderDateBetween(start, end);
        int totalPrice=1;
        for (ProductOrder order : list) {
            Product product = order.getProduct();
            if (product != null) {
                totalPrice += product.getPrice();
            }
        }
        return totalPrice;
    }
    public YearMonth MonthMinus(int num){
        LocalDate todayLocalDate = LocalDate.now();
        LocalDate y = todayLocalDate.minusMonths(num);
        YearMonth yearMonth = YearMonth.from(y);
        return yearMonth;
    }

}
