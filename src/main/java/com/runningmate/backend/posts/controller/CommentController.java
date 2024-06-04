package com.runningmate.backend.posts.controller;

import com.runningmate.backend.posts.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable(name = "commentId") Long commentId,
                           @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        commentService.deleteComment(commentId, username);
    }
}
