package com.runningmate.backend.posts.controller;

import com.runningmate.backend.posts.Post;
import com.runningmate.backend.posts.dto.CreatePostRequest;
import com.runningmate.backend.posts.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public Post createNewPost(@Valid @RequestBody CreatePostRequest createPostRequest, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return postService.createPost(createPostRequest, userDetails.getUsername());
    }

}
