package com.example.world.notice;


import com.example.world.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    @Value("${custom.genFileDirPath}")
    private  String genFileDirPath;

    public Page<Notice> allNotice(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page,10, Sort.by(sorts));
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

    public List<Notice> getNoticesByIds(Integer[] orderIds) {
        return noticeRepository.findByIdIn(orderIds);
    }




    public Notice create(String subject, String content, MultipartFile thumbnail) {

        String thumbnailRelPath = "notice"+UUID.randomUUID().toString()+".jpg";
        File thumbnailFile = new File(genFileDirPath+"/"+ thumbnailRelPath);

        thumbnailFile.mkdir();

        try {
            thumbnail.transferTo(thumbnailFile);
        } catch (IOException e){

        }



        Notice notice = new Notice();
        notice.setSubject(subject);
        notice.setContent(content);
        notice.setCreateDate(LocalDate.now());
        notice.setThumbnailImg(thumbnailRelPath);

        this.noticeRepository.save(notice);
        return notice;
    }

    public void modify(Notice notice, String subject, String content){
        notice.setSubject(subject);
        notice.setContent(content);
        notice.setModifyDate(LocalDate.now());
        this.noticeRepository.save(notice);
    }

    public void delete(List<Integer> orderIds) {
        noticeRepository.deleteByIds(orderIds);
    }


}
