package com.binno.dominio.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class LocalStorageService implements StorageProvider {

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            String separator = FileSystems.getDefault().getSeparator();
            URI uri = this.getClass().getResource(separator + "img").toURI();
            String path = Paths.get(uri).toString();
            Path copyLocation = Paths.get(path + File.separator + UUID.randomUUID());
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
            return copyLocation.toString();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("Não foi possivel salvar o arquivo no disco");
        }
    }

    public Resource loadAsResource(String filename) {
        return resourceLoader.getResource(this.getClass().getResource("img" + File.separator + filename).getFile());
    }
}
