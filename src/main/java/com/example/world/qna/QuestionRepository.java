package com.example.world.qna;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    Page<Question> findAll(Pageable pageable);

    Page<Question> findAll(Specification<Question> spec, Pageable pageable);

    @Query("select "
            + "distinct q "
            + "from Question q "
            + "left outer join SiteUser u1 on q.author=u1 "
            + "left outer join Answer a on a.question=q "
            + "left outer join SiteUser u2 on a.author=u2 "
            + "where "
            + "   q.subject like %:kw% "
            + "   or q.content like %:kw% "
            + "   or u1.nickname like %:kw% "
            + "   or a.content like %:kw% "
            + "   or u2.nickname like %:kw% ")
    Page<Question> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
}
