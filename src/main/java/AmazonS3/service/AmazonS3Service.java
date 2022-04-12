package AmazonS3.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface AmazonS3Service {

    String uploadFile(MultipartFile file);

    List<String> getObjectFromS3();

    InputStream downloadFile(String key);


}


