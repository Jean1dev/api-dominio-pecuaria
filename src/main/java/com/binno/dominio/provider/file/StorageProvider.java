package com.binno.dominio.provider.file;

import org.springframework.web.multipart.MultipartFile;

public interface StorageProvider {

    String uploadFile(MultipartFile file);
}
