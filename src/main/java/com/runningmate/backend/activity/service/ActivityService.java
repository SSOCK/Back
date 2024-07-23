package com.runningmate.backend.activity.service;

import com.runningmate.backend.activity.Activity;
import com.runningmate.backend.activity.dto.ListActivityResponseDto;
import com.runningmate.backend.activity.dto.NewAcitivityRequestDto;
import com.runningmate.backend.activity.dto.NewActivityResponseDto;
import com.runningmate.backend.activity.repository.ActivityRepository;
import com.runningmate.backend.dto.DataListResponseDTO;
import com.runningmate.backend.exception.NoPermissionException;
import com.runningmate.backend.exception.ResourceNotFoundException;
import com.runningmate.backend.member.Member;
import com.runningmate.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final MemberService memberService;

    @Transactional
    public ListActivityResponseDto getAllActivitiesInDescOrder(Long memberId) {
        Member member = memberService.getMemberById(memberId);
        List<Activity> activities = activityRepository.findAllByMemberOrderByCreatedAtDesc(member);
        List<NewActivityResponseDto> activityResponseDtos= activities.stream()
                .map(NewActivityResponseDto::fromEntity)
                .toList();

        return new ListActivityResponseDto(activityResponseDtos);
    }

    @Transactional
    public NewActivityResponseDto createNewActivity(NewAcitivityRequestDto requestDto, String username) {
        Member member = memberService.getMemberByUsername(username);
        Activity activity = requestDto.toEntity(member);
        return NewActivityResponseDto.fromEntity(activityRepository.save(activity));
    }

    @Transactional
    public NewActivityResponseDto getActivityById(Long activityId) {
        Activity activity = findActivityById(activityId);
        return NewActivityResponseDto.fromEntity(activity);
    }

    @Transactional
    public void deleteActivityById(Long activityId, String username) {
        Activity activity = findActivityById(activityId);
        Member member = activity.getMember();
        if(!member.getUsername().equals(username)) {
            throw new NoPermissionException("User " + username + " does not have permission to delete this activity");
        }
        activityRepository.delete(activity);
    }

    @Transactional
    public Activity findActivityById(Long activityId) {
        return activityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found"));
    }

}
