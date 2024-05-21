package com.runningmate.backend.posts.controller;

import com.runningmate.backend.member.Member;
import com.runningmate.backend.member.dto.PostResponseDto;
import com.runningmate.backend.member.service.MemberService;
import com.runningmate.backend.posts.Post;
import com.runningmate.backend.posts.dto.CreatePostRequest;
import com.runningmate.backend.posts.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final MemberService memberService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public PostResponseDto createNewPost(@Valid @RequestBody CreatePostRequest createPostRequest, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return postService.createPost(createPostRequest, userDetails.getUsername());
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

}
