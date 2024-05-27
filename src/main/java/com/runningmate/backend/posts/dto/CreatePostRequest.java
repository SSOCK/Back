package com.runningmate.backend.posts.dto;

import com.runningmate.backend.member.Member;
import com.runningmate.backend.posts.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatePostRequest {

    @NotBlank(message = "Title cannot be Empty")
    @Size(min=1, max=100)
    private String title;

    @Size(max=2000)
    private String content;

    private MultipartFile image;

    public Post toEntity(Member member, String imageUrl) {
        return Post.builder()
                .member(member)
                .title(this.title)
                .content(this.content)
                .imageUrl(imageUrl)
                .build();
    }
}
