package com.runningmate.backend.posts.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.runningmate.backend.member.Member;
import com.runningmate.backend.posts.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatePostRequest {

    @NotBlank(message = "Title cannot be Empty")
    @Size(min=1, max=100)
    private String title;

    @Size(max=2000)
    private String content;

    private List<MultipartFile> image = new ArrayList<>();

    public Post toEntity(Member member, List<String> imageUrls) {
        return Post.builder()
                .member(member)
                .title(this.title)
                .content(this.content)
                .imageUrls(imageUrls)
                .build();
    }
}
