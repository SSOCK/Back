package com.runningmate.backend.posts.service;

import com.runningmate.backend.exception.NoPermissionException;
import com.runningmate.backend.exception.ResourceNotFoundException;
import com.runningmate.backend.member.Member;
import com.runningmate.backend.member.service.MemberService;
import com.runningmate.backend.posts.Comment;
import com.runningmate.backend.posts.Post;
import com.runningmate.backend.posts.dto.CommentRequestDto;
import com.runningmate.backend.posts.dto.CommentResponseDto;
import com.runningmate.backend.posts.dto.PostResponseDto;
import com.runningmate.backend.posts.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final MemberService memberService;
    private final PostService postService;
    private final CommentRepository commentRepository;

    @Transactional
    public PostResponseDto saveComment(String username, CommentRequestDto dto, Long postId) {
        Member member = memberService.getMemberByUsername(username);
        Post post = postService.getPostById(postId);

        Comment comment = dto.toEntity(member, post);
        commentRepository.save(comment);
        //This causes unnecessary fetching but leave at is for now
        PostResponseDto updatedPostDto = postService.getOnePost(post.getId(), member);
        return updatedPostDto;
    }

    public void deleteComment(Long commentId, String username) {
        Comment comment = getCommentById(commentId);

        if (!hasPermission(comment, username)) {
            throw new NoPermissionException();
        }

        commentRepository.delete(comment);
    }

    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with comment id " + commentId));
    }

    private boolean hasPermission(Comment comment, String username) {
        if (comment.getMember().getUsername().equals(username)) {
            return true;
        }
        return false;
    }
}
