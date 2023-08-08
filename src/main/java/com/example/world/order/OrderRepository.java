package com.example.world.order;

import com.example.world.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<ProductOrder, Long> {
    Optional<ProductOrder> findByProduct(Product product);

}
