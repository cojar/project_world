package com.example.world.file;

import com.example.world.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FileService {

    @Value("${file.root.path}")
    private String fileRootPath;

    @Value("${file.origin.path}")
    private String fileOriginPath;

    private final FileRepository fileRepository;

    //    public UploadedFile upload(MultipartFile file, String primaryPath, String secondaryPath, String uploader) throws IOException {
    public UploadedFile upload(MultipartFile file, String primaryPath, String uploader) throws IOException {

        String[] fileBits = file.getContentType().split("/");
        String type = fileBits[0];
        String ext;

        switch (fileBits[1]) {
            case "jpg", "jpeg" -> ext = "jpg";
            case "png" -> ext = "png";
            default -> ext = "etc";
        }

        // file type == image
        // file ext == jpg, jpeg, png

        if (!type.equals("image") || ext.equals("etc")) {
            throw new IOException("file type not allowed");
        }

        // file path == root/primaryPath/secondaryPath
        // file name == uploader_date.ext;

        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_n"));

//        String saveDirPath = fileRootPath
//                + File.separator + primaryPath
//                + File.separator + secondaryPath;

        String saveDirPath = fileRootPath
                + File.separator + primaryPath;

        String saveFilePath = saveDirPath
                + File.separator + uploader
                + "_" + date
                + "." + ext;

        file.transferTo(new File(saveFilePath));

        UploadedFile uploadedFile = new UploadedFile();

        uploadedFile.setPrimaryPath(primaryPath);
//        uploadedFile.setSecondaryPath(secondaryPath);
        uploadedFile.setUploader(uploader);
        uploadedFile.setDate(date);
        uploadedFile.setExt(ext);
        uploadedFile.setCreateDate(LocalDateTime.now());

        this.fileRepository.save(uploadedFile);

        return uploadedFile;
    }

    public UploadedFile getFile(Long id) {
        Optional<UploadedFile> ou = this.fileRepository.findById(id);
        if (ou.isPresent()) {
            return ou.get();
        } else {
            throw new DataNotFoundException("file not found");
        }
    }

    public String getFilePath(UploadedFile uploadedFile) {
        return fileOriginPath + uploadedFile.getPrimaryPath()
//                + File.separator + uploadedFile.getSecondaryPath()
                + File.separator + uploadedFile.getUploader()
                + "_" + uploadedFile.getDate()
                + "." + uploadedFile.getExt();
    }
}