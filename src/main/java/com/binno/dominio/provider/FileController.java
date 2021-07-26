package com.binno.dominio.provider;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "file")
public class FileController {

    private final StorageProvider storageService;

    public FileController(@Qualifier("s3") StorageProvider storageService) {
        this.storageService = storageService;
    }

    @PostMapping(path = "upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        return storageService.uploadFile(file);
    }
}
