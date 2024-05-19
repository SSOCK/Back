package com.runningmate.backend.posts.repository;

import com.runningmate.backend.posts.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    List<PostLike> findByPostId(Long postId);
    long countByPostId(Long postId);
    List<PostLike> findByMemberId(Long memberId);
    boolean existsByPostIdAndMemberId(Long postId, Long memberId);
    void deleteByPostIdAndMemberId(Long postId, Long memberId);

}
