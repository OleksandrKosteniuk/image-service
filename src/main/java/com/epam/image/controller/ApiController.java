package com.epam.image.controller;

import com.epam.image.service.DynamoDbService;
import com.epam.image.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private final S3Service s3Service;
    private final DynamoDbService dynamoDbService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        Path tempFile = Files.createTempFile(file.getOriginalFilename(), "");
        file.transferTo(tempFile);
        s3Service.uploadFile(tempFile);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/image/{label}")
    public ResponseEntity<?> getImagesByLabel(@PathVariable String label) {
        return ResponseEntity.ok(dynamoDbService.getImagesByLabel(label));
    }
}
