package com.runningmate.backend.posts.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.runningmate.backend.member.Member;
import com.runningmate.backend.posts.Comment;
import com.runningmate.backend.posts.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentRequestDto {
    @NotBlank(message = "Content has to include non-whitespace characters")
    @Size(max = 2000)
    private String content;

    public Comment toEntity(Member member, Post post) {
        Comment comment = Comment.builder()
                .member(member)
                .post(post)
                .content(content)
                .build();
        return comment;
    }
}
