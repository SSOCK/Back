package com.runningmate.backend.posts;

import com.runningmate.backend.entity.BaseTimeEntity;
import com.runningmate.backend.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "post")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ElementCollection
    @CollectionTable(name = "post_images", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "image_url")
    @Builder.Default
    private List<String> imageUrls = new ArrayList<>(); // Add this field

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OrderBy("createdAt asc") //Order by time, 시간 순으로 정렬
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    public void addImageUrl(String imageUrl) {
        imageUrls.add(imageUrl);
    }

    public void removeImageUrl(String imageUrl) {
        imageUrls.remove(imageUrl);
    }

    public void updatePost(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
