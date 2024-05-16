package com.runningmate.backend.posts.dto;

import com.runningmate.backend.member.Member;
import com.runningmate.backend.posts.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatePostRequest {

    @NotBlank(message = "Title cannot be Empty")
    @Size(min=1, max=100)
    private String title;

    @Size(max=2000)
    private String content;

    public Post toEntity(Member member) {
        return Post.builder()
                .member(member)
                .title(this.title)
                .content(this.content)
                .build();
    }
}
