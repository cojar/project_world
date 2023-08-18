package com.example.world.order;

import com.example.world.product.Product;
import com.example.world.qna.Question;
import com.example.world.user.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<ProductOrder, Long> {
    long countByOrderStatus(String orderStatus);

    List<ProductOrder> findByOrderDateBetween(LocalDateTime start, LocalDateTime end);

    Page<ProductOrder> findAll(Pageable pageable);


    Optional<ProductOrder> findByProduct(Product product);

    List<ProductOrder> findByUser(SiteUser siteUser);
}
