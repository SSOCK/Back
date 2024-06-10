package com.runningmate.backend.posts.dto;

import com.runningmate.backend.member.dto.MemberDto;
import com.runningmate.backend.posts.Post;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private MemberDto member;
    private List<String> imageUrls;
    private long likes;
    private List<CommentResponseDto> comments;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static PostResponseDto fromEntity(Post post, long likes) {
        MemberDto memberDto = MemberDto.fromEntity(post.getMember());

        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .likes(likes)
                .comments(post.getComments().stream().map(CommentResponseDto::new).collect(Collectors.toList()))
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .imageUrls(post.getImageUrls())
                .member(memberDto)
                .build();
    }
}
