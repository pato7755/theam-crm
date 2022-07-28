package com.theam.crm.config.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

@Service
public class MinioService {

    @Autowired
    MinioClient minioClient;

    @Value("${minio.bucket.name}")
    String bucketName;

    @Value("${minio.url}")
    String endpointUrl;

//    @Value("${minio.default.folder}")
//    String baseFolder;

    public String uploadFile(MultipartFile multipartFile) {
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
            uploadFileToMinIOBucket(fileName, file, multipartFile);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("url: " + fileUrl);
        return fileUrl;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    private void uploadFileToMinIOBucket(String fileName, File file, MultipartFile multipartFile) throws Exception {
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                .build());
    }


//    public void uploadFile(String name, byte[] content) {
//        File file = new File("/tmp/" + name);
//        file.canWrite();
//        file.canRead();
//        try {
//            FileOutputStream iofs = new FileOutputStream(file);
//            iofs.write(content);
//            minioClient.putObject(defaultBucketName, defaultBaseFolder + name, file.getAbsolutePath());
//        } catch (Exception e) {
//           throw new RuntimeException(e.getMessage());
//        }
//
//    }
//
//    public byte[] getFile(String key) {
//        try {
//            InputStream obj = minioClient.getObject(defaultBucketName, defaultBaseFolder + "/" + key);
//
//            byte[] content = IOUtils.toByteArray(obj);
//            obj.close();
//            return content;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @PostConstruct
//    public void init() {
//    }
}