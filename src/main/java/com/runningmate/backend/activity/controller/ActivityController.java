package com.runningmate.backend.activity.controller;

import com.runningmate.backend.activity.dto.ListActivityResponseDto;
import com.runningmate.backend.activity.dto.NewAcitivityRequestDto;
import com.runningmate.backend.activity.dto.NewActivityResponseDto;
import com.runningmate.backend.activity.service.ActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;

    @GetMapping("/member/{memberId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ListActivityResponseDto getAllActivitiesInDescOrder(@PathVariable(name = "memberId") Long memberId,
                                                                            @AuthenticationPrincipal UserDetails userDetails) {
        return activityService.getAllActivitiesInDescOrder(memberId);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public NewActivityResponseDto createActivity(@Valid @RequestBody NewAcitivityRequestDto newActivityRequestDto,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        return activityService.createNewActivity(newActivityRequestDto, userDetails.getUsername());
    }

    @GetMapping("/{activityId}")
    @ResponseStatus(value = HttpStatus.OK)
    public NewActivityResponseDto getActivityById(@PathVariable(name = "activityId") Long activityId) {
        return activityService.getActivityById(activityId);
    }

    @DeleteMapping("/{activityId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteActivityById(@PathVariable(name = "activityId") Long activityId,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        activityService.deleteActivityById(activityId, userDetails.getUsername());
    }
}
