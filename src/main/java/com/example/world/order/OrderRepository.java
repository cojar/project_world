package com.example.world.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<ProductOrder, Long> {

    List<ProductOrder> findByOrderDateBetween(LocalDateTime start, LocalDateTime end);




}
