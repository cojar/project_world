package com.example.world.product;


import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

 Page<Product> findAll(Specification<Product> spec, Pageable pageable );
 Page<Product> findAll(Pageable pageable);
}
