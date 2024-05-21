package com.runningmate.backend.member.dto;

import com.runningmate.backend.posts.Post;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private MemberDto member;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static PostResponseDto fromEntity(Post post) {
        MemberDto memberDto = MemberDto.fromEntity(post.getMember());

        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .member(memberDto)
                .build();
    }
}
