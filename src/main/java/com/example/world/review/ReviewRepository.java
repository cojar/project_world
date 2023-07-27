package com.example.world.review;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
//    List<ProductReview> findByAuthor(SiteUser author);
}
