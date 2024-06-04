package com.runningmate.backend.posts.service;

import com.runningmate.backend.exception.NoPermissionException;
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
        Post post = postRequest.toEntity(member, imageUrl);
        Post savedPost = postRepository.save(post);
        return PostResponseDto.fromEntity(savedPost, 0);
    }

    public Post updatePost(Long id, Post updatePostDTO) {//TODO: change to PostDTO
        Post post = this.getPostById(id);
        post.updatePost(updatePostDTO.getTitle(), updatePostDTO.getContent());
        return postRepository.save(post);
    }

    public List<PostResponseDto> getRecentPostsOfFollowedMembers(Member user, int page, int size) {
        List<Follow> follows = followRepository.findByFollower(user);
        List<Member> followedMembers = follows.stream()
                .map(Follow::getFollowing)
                .collect(Collectors.toList());

        if (size > 30) {
            size = 30;
        }
        Pageable pageable = PageRequest.of(page, size);
        List<Post> posts = postRepository.findByMemberInOrderByCreatedAtDesc(followedMembers, pageable);

        return posts.stream().map((Post post) -> PostResponseDto.fromEntity(post, postLikeService.getLikeCountByPostId(post.getId()))).collect(Collectors.toList());
    }

    public PostResponseDto getOnePost(Long postId) {
        Post post = getPostById(postId);
        long likes = postLikeService.getLikeCountByPostId(postId);
        return PostResponseDto.fromEntity(post, likes);
    }

    public boolean deletePost(Long postId, String username) {
        Post post = getPostById(postId);
        if(!hasPermission(post, username)) {
            throw new NoPermissionException();
        }
        postRepository.delete(post);
        return true;
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + postId));
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

    private boolean hasPermission(Post post, String username) {
        if(!post.getMember().getUsername().equals(username)) {
            return false;
        }
        return true;
    }
}
