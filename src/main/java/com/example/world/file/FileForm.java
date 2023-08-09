package com.example.world.file;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileForm {
    MultipartFile file;
}
