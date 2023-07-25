package com.example.world.notice;

import com.example.world.user.SiteUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/list")
    public String noticeList(Model model, @RequestParam(value = "page", defaultValue = "0")int page){
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

    @GetMapping("/create")
    public String noticeCreate(){
        return "notice_form";
    }


    @PostMapping("/create")
    public String articleCreate(@Valid NoticeForm noticeForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "notice_form";
        }
        this.noticeService.create(noticeForm.getSubject(),noticeForm.getContent());
        return String.format("redirect:/notice/list");
    }
}
