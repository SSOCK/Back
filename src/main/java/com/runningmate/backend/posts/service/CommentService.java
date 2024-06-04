package com.runningmate.backend.posts.service;

import com.runningmate.backend.member.Member;
import com.runningmate.backend.member.service.MemberService;
import com.runningmate.backend.posts.Comment;
import com.runningmate.backend.posts.Post;
import com.runningmate.backend.posts.dto.CommentRequestDto;
import com.runningmate.backend.posts.dto.CommentResponseDto;
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
    public CommentResponseDto saveComment(String username, CommentRequestDto dto, Long postId) {
        Member member = memberService.getMemberByUsername(username);
        Post post = postService.getPostById(postId);

        Comment comment = dto.toEntity(member, post);
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }
}
