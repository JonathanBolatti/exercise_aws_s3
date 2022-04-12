package AmazonS3.controller;

import AmazonS3.service.AmazonS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/storage")
public class AmazonS3Controller {

    @Autowired
    private AmazonS3Service s3Service;

    @PostMapping(value = "/upload")
    public ResponseEntity<String> uploadFile(@RequestPart(value = "file") MultipartFile file) {
        String url = s3Service.uploadFile(file);
        String response = "The file: " + "\"" + file.getOriginalFilename() + "\"" + " was successfully uploaded to AWS S3. "
                + "\nThe generated link is: " + url;
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping(value = "/list")
    public ResponseEntity<List<String>> listFiles() {
        return new ResponseEntity(s3Service.getObjectFromS3(), HttpStatus.OK);
    }

    @GetMapping(value = "/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("key") String key) {
        InputStreamResource resource = new InputStreamResource(s3Service.downloadFile(key));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + key + "\"").body(resource);
    }

}


