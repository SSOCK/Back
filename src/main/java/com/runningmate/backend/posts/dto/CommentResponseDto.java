package com.runningmate.backend.posts.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.runningmate.backend.posts.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long id;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private Long postId;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.username = comment.getMember().getUsername();
        this.createdAt = comment.getCreatedAt();
        this.postId = comment.getPost().getId();
    }
}
