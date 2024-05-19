package com.runningmate.backend.posts.service;

import com.runningmate.backend.exception.ResourceNotFoundException;
import com.runningmate.backend.member.Member;
import com.runningmate.backend.member.service.MemberService;
import com.runningmate.backend.posts.Post;
import com.runningmate.backend.posts.dto.CreatePostRequest;
import com.runningmate.backend.posts.repository.PostLikeRepository;
import com.runningmate.backend.posts.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostLikeService postLikeService;
    private final MemberService memberService;


    public Post createPost(CreatePostRequest postRequest, String username) {
        Member member = memberService.getMemberByUsername(username);
        Post post = postRequest.toEntity(member);
        return postRepository.save(post);
    }

    public Post updatePost(Long id, Post updatePostDTO) {//TODO: change to PostDTO
        return this.getPostById(id)
                .map(post -> {
                    post.updatePost(updatePostDTO.getTitle(), updatePostDTO.getContent());
                    return postRepository.save(post);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + id));
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public boolean toggleLike(Long postId, String username) {
        Member member = memberService.getMemberByUsername(username);
        return postLikeService.toggleLike(postId, member.getId());
    }

    //TODO: Might have to let the client know if a post with Id does not exist.
//    public void deletePost(Long id) {
//        postRepository.deleteById(id);
//    }

    public List<Post> getPostsByMember(Member member) {
        return postRepository.findAllByMember(member);
    }

    public List<Post> getPostsByCreatedAt(LocalDate date) {
        return postRepository.findAllByCreatedAt(date);
    }
}
