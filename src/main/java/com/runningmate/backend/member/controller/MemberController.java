package com.runningmate.backend.member.controller;

import com.runningmate.backend.member.Member;
import com.runningmate.backend.member.dto.MemberListResponseDto;
import com.runningmate.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/{memberId}/follow")
    public void followUser(@PathVariable(name = "memberId") Long memberId, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        Member follower = memberService.getMemberByUsername(username);
        Member following = memberService.getMemberById(memberId);
        memberService.followUser(follower, following);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping("/{memberId}/unfollow")
    public void unfollowUser(@PathVariable(name = "memberId") Long memberId, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        Member follower = memberService.getMemberByUsername(username);
        Member following = memberService.getMemberById(memberId);
        memberService.unfollowUser(follower, following);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/{memberId}/followers")
    public MemberListResponseDto getFollowerList(@PathVariable(name = "memberId") Long memberId) {
        return memberService.getFollowerList(memberId);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/{memberId}/followings")
    public MemberListResponseDto getFollowingList(@PathVariable(name = "memberId") Long memberId) {
        return memberService.getFollowingList(memberId);
    }
}
