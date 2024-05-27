package com.runningmate.backend.posts.service;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.*;
import com.runningmate.backend.exception.InvalidFileTypeException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class GcsFileStorageService {
    private static final List<String> ACCEPTED_CONTENT_TYPES = Arrays.asList("image/jpeg", "image/png", "image/gif");

    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String keyFileName;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    public String storeFile(MultipartFile file) throws RuntimeException, IOException {
        if (!validateFile(file)) {
            return null;
        }

        InputStream keyFile = ResourceUtils.getURL(keyFileName).openStream();

        String fileName = UUID.randomUUID().toString();
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();

        Storage storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(keyFile))
                .build()
                .getService();

        try (WriteChannel writer = storage.writer(blobInfo)){
            byte[] fileData = file.getBytes();
            writer.write(ByteBuffer.wrap(fileData));
            return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file in GCS", e); // throws 500
        }
    }

    private boolean validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        if (!ACCEPTED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new InvalidFileTypeException(file.getContentType());
        }
        String filename = file.getOriginalFilename();
        if (filename != null && !filename.toLowerCase().matches(".*\\.(jpg|jpeg|png|gif)$")) {
            throw new InvalidFileTypeException(file.getContentType(), filename.toLowerCase());
        }
        return true;
    }
}