package com.example.world.admin;

import com.example.world.order.ProductOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<ProductOrder, Long> {
    Page<ProductOrder> findAll(Pageable pageable);
}
