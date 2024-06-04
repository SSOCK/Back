package com.runningmate.backend.posts.dto;

import com.runningmate.backend.member.dto.MemberDto;
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
    private String imageUrl;
    private long likes;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static PostResponseDto fromEntity(Post post, long likes) {
        MemberDto memberDto = MemberDto.fromEntity(post.getMember());

        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .likes(likes)
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .imageUrl(post.getImageUrl())
                .member(memberDto)
                .build();
    }
}
