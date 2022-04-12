package AmazonS3.service.impl;

import AmazonS3.service.AmazonS3Service;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AmazonS3ServiceImpl implements AmazonS3Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmazonS3ServiceImpl.class);

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${BUCKET_NAME}")
    private String bucketName;

    @Value("${ENDPOINT_URL}")
    private String endpointUrl;

    @Value("${S3_REGION}")
    private String s3Region;

    @Override
    public String uploadFile(MultipartFile file) {
        File createFile = new File(file.getOriginalFilename());
        String fileUrl = "";
        try (FileOutputStream stream = new FileOutputStream(createFile)) {
            stream.write(file.getBytes());
            String newFileName = System.currentTimeMillis() + createFile.getName();
            LOGGER.info("uploading file with name: " + newFileName);
            fileUrl = bucketName + ".s3." + s3Region + "." + endpointUrl + "/" + newFileName;
            PutObjectRequest request = new PutObjectRequest(bucketName, newFileName, createFile);
            amazonS3.putObject(request);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return fileUrl;
    }

    @Override
    public List<String> getObjectFromS3() {
        ListObjectsV2Result result = amazonS3.listObjectsV2(bucketName);
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        List<String> list = objects.stream().map(item -> {
            return item.getKey();
        }).collect(Collectors.toList());
        return list;
    }

    @Override
    public InputStream downloadFile(String key) {
        S3Object s3Object = amazonS3.getObject(bucketName, key);
        return s3Object.getObjectContent();
    }
}
