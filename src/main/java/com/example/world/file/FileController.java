package com.example.world.file;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequestMapping("/file")
@RequiredArgsConstructor
@Controller
public class FileController {

    private final FileService fileService;

    @GetMapping("")
    public String upload(Model model, FileForm fileForm) {
        UploadedFile file = this.fileService.getFile(1L);
        String path = this.fileService.getFilePath(file);
        model.addAttribute("path", path);
        return "file";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String upload(@Valid FileForm fileForm, BindingResult bindingResult) throws IOException {
        MultipartFile file = fileForm.getFile();
        UploadedFile uploadedFile = this.fileService.upload(file, "product", "productImage", "world");
        return this.fileService.getFilePath(uploadedFile);
    }
}
