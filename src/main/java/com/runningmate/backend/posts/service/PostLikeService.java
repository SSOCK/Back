package com.runningmate.backend.posts.service;

import com.runningmate.backend.member.Member;
import com.runningmate.backend.posts.Post;
import com.runningmate.backend.posts.PostLike;
import com.runningmate.backend.posts.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;

    public List<PostLike> getLikesByPostId(Long postId) {
        return postLikeRepository.findByPostId(postId);
    }

    public boolean hasMemberLikedPost(Long postId, Long memberId) {
        return postLikeRepository.existsByPostIdAndMemberId(postId, memberId);
    }

    public long getLikeCountByPostId(Long postId) {
        return postLikeRepository.countByPostId(postId);
    }

    public void removeLikeByMember(Long postId, Long memberId) {
        postLikeRepository.deleteByPostIdAndMemberId(postId, memberId);
    }

    public List<PostLike> getLikesByMemberId(Long memberId) {
        return postLikeRepository.findByMemberId(memberId);
    }

    @Transactional
    public boolean toggleLike(Long postId, Long memberId) {
        if (postLikeRepository.existsByPostIdAndMemberId(postId, memberId)) {
            postLikeRepository.deleteByPostIdAndMemberId(postId, memberId);
            return false;
        } else {
            PostLike postLike = PostLike.builder()
                    .post(Post.builder().id(postId).build())
                    .member(Member.builder().id(memberId).build())
                    .build();
            postLikeRepository.save(postLike);
            return true;
        }
    }

}
