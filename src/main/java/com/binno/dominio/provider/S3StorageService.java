package com.binno.dominio.provider;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Qualifier("s3")
public class S3StorageService implements StorageProvider {

    private static final Logger LOG = LoggerFactory.getLogger(S3StorageService.class);

    @Autowired
    private AmazonS3 s3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile multipartFile) {
        String keyName = UUID.randomUUID().toString() + ".jpg";
        File file = new File(keyName);

        try {
            Path copyLocation = Paths.get(file.getAbsolutePath());
            Files.copy(multipartFile.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
            s3.putObject(bucketName, keyName, new File(keyName));
            s3.setObjectAcl(bucketName, keyName, CannedAccessControlList.PublicRead);
            LOG.info("upload finalizado");

            file.delete();
            return s3.getUrl(bucketName, keyName).toURI().toString();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
