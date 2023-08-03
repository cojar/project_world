package com.example.world.notice;


import com.example.world.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public Page<Notice> allNotice(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page,10);
        return this.noticeRepository.findAll(pageable);
    }

    public Notice getNotice(int id){
        Optional<Notice> getId = noticeRepository.findById(id);
        if(getId.isPresent()){
            return getId.get();
        }else{
            throw new DataNotFoundException("question not found");
        }
    }

    public void create(String subject, String content) {
        Notice notice = new Notice();
        notice.setSubject(subject);
        notice.setContent(content);
        notice.setCreateDate(LocalDate.now());
        this.noticeRepository.save(notice);
    }
}
