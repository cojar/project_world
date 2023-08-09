package com.example.world.qna;

import com.example.world.DataNotFoundException;
import com.example.world.product.Product;
import com.example.world.product.ProductService;
import com.example.world.qnaAnswer.Answer;
import com.example.world.qnaAnswer.AnswerRepository;
import com.example.world.user.SiteUser;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final ProductService productService;
    private final AnswerRepository answerRepository;

    public Page<Question> getListByProductId(Long productId, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.questionRepository.findByProduct_Id(productId, pageable);
    }

    public Page<Question> getList(int page, int size) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, size, Sort.by(sorts));
        return this.questionRepository.findAll(pageable);
    }

    public List<Question> getQuestionList() {
        return this.questionRepository.findAll();
    }

    public Question getQuestion(Long id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    public void create(String content, SiteUser user, Long productId) {
        Product product = productService.getProduct(productId);
        Question q = new Question();
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(user);
        q.setAnswered(false);
        q.setProduct(product);
        this.questionRepository.save(q);
    }

    public void modify(Question question, String content) {
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }

    public void delete(Question question) {
        this.questionRepository.delete(question);
    }

    @Transactional
    public void deleteQuestionWithAnswers(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question not found with id: " + questionId));

        List<Answer> answerList = question.getAnswerList();
        if (answerList != null && !answerList.isEmpty()) {
            this.answerRepository.deleteAll(answerList);
        }
        questionRepository.delete(question);
    }

    public List<Question> getAuthor(SiteUser siteUser) {
        return this.questionRepository.findByAuthor(siteUser);
    }
}