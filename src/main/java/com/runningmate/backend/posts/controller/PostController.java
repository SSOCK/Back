package com.runningmate.backend.posts.controller;

import com.runningmate.backend.member.Member;
import com.runningmate.backend.posts.dto.CommentRequestDto;
import com.runningmate.backend.posts.dto.CommentResponseDto;
import com.runningmate.backend.posts.dto.PostResponseDto;
import com.runningmate.backend.member.service.MemberService;
import com.runningmate.backend.posts.dto.CreatePostRequest;
import com.runningmate.backend.posts.service.CommentService;
import com.runningmate.backend.posts.service.GcsFileStorageService;
import com.runningmate.backend.posts.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final MemberService memberService;
    private final CommentService commentService;
    private final GcsFileStorageService gcsFileStorageService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public PostResponseDto createNewPost(@Valid @ModelAttribute CreatePostRequest createPostRequest, @AuthenticationPrincipal UserDetails userDetails) throws RuntimeException, IOException {
        String username = userDetails.getUsername();
        String imageUrl = gcsFileStorageService.storeFile(createPostRequest.getImage());
        return postService.createPost(createPostRequest, username, imageUrl);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public List<PostResponseDto> getRecentPosts(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        Member user = memberService.getMemberByUsername(username);
        return postService.getRecentPostsOfFollowedMembers(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{postId}/like")
    public void toggleLike(@PathVariable(name = "postId") Long postId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        postService.toggleLike(postId, userDetails.getUsername());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{postId}/comments")
    public CommentResponseDto commentOnPost(@PathVariable(name = "postId") Long postId, @Valid @RequestBody CommentRequestDto commentRequestDto, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return commentService.saveComment(userDetails.getUsername(), commentRequestDto, postId);
    }

}
