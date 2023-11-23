package com.example.world.notice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice,Integer> {
    Page<Notice> findAll(Pageable pageable);

    List<Notice> findByIdIn(Integer[] orderIds);


    @Transactional
    @Modifying
    @Query("DELETE FROM Notice n WHERE n.id IN :orderIds")
    void deleteByIds(@Param("orderIds") List<Integer> orderIds);


}
