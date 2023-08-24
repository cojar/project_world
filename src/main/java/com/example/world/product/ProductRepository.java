package com.example.world.product;


import com.example.world.user.SiteUser;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

 Page<Product> findAll(Specification<Product> spec, Pageable pageable );
 Page<Product> findAll(Pageable pageable);
 List<Product> findByWish(SiteUser wish);

 Page<Product> findByWish(Pageable pageable, SiteUser siteUser);

 @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.wish")
 Page<Product> findAllWithWishes(Pageable pageable);
}
