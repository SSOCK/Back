package com.runningmate.backend.image;

import com.runningmate.backend.posts.service.GcsFileStorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {
    private final GcsFileStorageService gcsFileStorageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ImageUploadResponse uploadImage(@Valid @ModelAttribute ImageUploadRequest request,
                                           @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        String url = gcsFileStorageService.storeFile(request.getImage());
        return new ImageUploadResponse(url);
    }
}
