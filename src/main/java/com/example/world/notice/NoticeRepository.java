package com.example.world.notice;

import com.example.world.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice,Integer> {
    Page<Notice> findAll(Pageable pageable);
}
