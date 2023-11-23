package com.example.world.qna;

import com.example.world.user.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {


    Question findByContent(String content);
    Page<Question> findAll(Pageable pageable);
    Page<Question> findByProduct_Id(Long productId, Pageable pageable);

    List<Question> findByAuthor(SiteUser siteUser);

    Page<Question> findByAuthor(Pageable pageable , SiteUser siteUser);
}
