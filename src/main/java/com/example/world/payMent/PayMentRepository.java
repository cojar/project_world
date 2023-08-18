package com.example.world.payMent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayMentRepository extends JpaRepository<PayMent, Long> {
}
