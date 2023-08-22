package com.example.world.notice;

import com.example.world.product.productImage.ProductImageForm;
import com.example.world.user.SiteUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;


    @GetMapping("/list")
    public String noticeList(Model model, @RequestParam(value = "page", defaultValue = "0")int page){
        //
        Page<Notice> paging = this.noticeService.allNotice(page);
        model.addAttribute("paging",paging);
        return "notice_list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") int id){
        Notice notice = noticeService.getNotice(id);
        model.addAttribute("notice", notice);
        return "notice_detail";
    }

//    @GetMapping("/create")
//    public String noticeCreate(){
//        return "notice_form";
//    }
//
//
//    @PostMapping("/create")
//    public String articleCreate(@Valid NoticeForm noticeForm, BindingResult bindingResult, MultipartFile thumbnail) {
//        if (bindingResult.hasErrors()) {
//            return "notice_form";
//        }
//        this.noticeService.create(noticeForm.getSubject(),noticeForm.getContent(),thumbnail);
//        return String.format("redirect:/admin/notice");
//    }


    @GetMapping("/modify/{id}")
    public String noticeModify(NoticeForm noticeForm, @PathVariable("id") Integer id) {
        Notice notice = this.noticeService.getNotice(id);
        noticeForm.setSubject(notice.getSubject());
        noticeForm.setContent(notice.getContent());
        return "notice_form";
    }


    @PostMapping("/modify/{id}")
    public String noticeModify(@Valid NoticeForm noticeForm, BindingResult bindingResult,
                                 @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "notice_form";
        }
        Notice notice = this.noticeService.getNotice(id);
        this.noticeService.modify(notice, noticeForm.getSubject(), noticeForm.getContent());
        return String.format("redirect:/ad/notice");
    }

    @GetMapping("/delete")
    @ResponseBody
    public Page<Notice> noticeDelete(@RequestParam(value = "orderIds[]") List<Integer> orderIds, @RequestParam(value = "page", defaultValue = "0") int page) {
//        List<Notice> notices = noticeService.getNoticesByIds(orderIds);
        this.noticeService.delete(orderIds);
        Page<Notice> noticeList = this.noticeService.allNotice(page);
        return noticeList;
    }




}
