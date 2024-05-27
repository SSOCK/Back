package com.runningmate.backend.posts.service;

import com.runningmate.backend.exception.ResourceNotFoundException;
import com.runningmate.backend.member.Follow;
import com.runningmate.backend.member.Member;
import com.runningmate.backend.posts.dto.PostResponseDto;
import com.runningmate.backend.member.repository.FollowRepository;
import com.runningmate.backend.member.service.MemberService;
import com.runningmate.backend.posts.Post;
import com.runningmate.backend.posts.dto.CreatePostRequest;
import com.runningmate.backend.posts.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostLikeService postLikeService;
    private final MemberService memberService;
    private final FollowRepository followRepository;


    public PostResponseDto createPost(CreatePostRequest postRequest, String username, String imageUrl) {
        Member member = memberService.getMemberByUsername(username);
        Post post = postRequest.toEntity(Member.builder().id(member.getId()).build(), imageUrl);
        Post savedPost = postRepository.save(post);
        return PostResponseDto.fromEntity(savedPost);
    }

    public Post updatePost(Long id, Post updatePostDTO) {//TODO: change to PostDTO
        return this.getPostById(id)
                .map(post -> {
                    post.updatePost(updatePostDTO.getTitle(), updatePostDTO.getContent());
                    return postRepository.save(post);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + id));
    }

    public List<PostResponseDto> getRecentPostsOfFollowedMembers(Member user) {
        List<Follow> follows = followRepository.findByFollower(user);
        List<Member> followedMembers = follows.stream()
                .map(Follow::getFollowing)
                .collect(Collectors.toList());

        Pageable pageable = PageRequest.of(0, 30);
        List<Post> posts = postRepository.findByMemberInOrderByCreatedAtDesc(followedMembers, pageable);

        return posts.stream().map(PostResponseDto::fromEntity).collect(Collectors.toList());
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
