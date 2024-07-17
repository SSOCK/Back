package com.runningmate.backend.club.controller;

import com.runningmate.backend.club.dto.*;
import com.runningmate.backend.club.service.ClubService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/club")
@RequiredArgsConstructor
public class ClubController {
    /*TODO: join club(check already in), leave club(owner can't leave), delete club, change owner,
       remove member, update title/description, get club info, create new schedule, update schedule
     */
    private final ClubService clubService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ClubResponseDto createNewClub(@RequestBody ClubRequestDto clubRequestDto,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        ClubResponseDto clubResponseDto = clubService.createClub(clubRequestDto, userDetails.getUsername());
        return clubResponseDto;
    }

    @GetMapping("/{clubId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ClubResponseDto getClub(@PathVariable(name = "clubId") UUID clubId,
                        @AuthenticationPrincipal UserDetails userDetails) {
        return clubService.getClub(clubId);
    }

    @GetMapping("/{clubId}/schedules")
    @ResponseStatus(value = HttpStatus.OK)
    public ListClubScheduleEntityResponseDto getClubSchedules(@PathVariable(name = "clubId") UUID clubId,
                                                          @AuthenticationPrincipal UserDetails userDetails) {
        return clubService.getClubSchedules(clubId, userDetails.getUsername());
    }

    @PostMapping("/{clubId}/schedules")
    @ResponseStatus(HttpStatus.OK)
    public ClubScheduleEntityResponseDto createClubSchedule(@PathVariable(name = "clubId") UUID clubId,
                                                            @Valid @RequestBody CreateOrUpdateClubScheduleRequestDto requestDto,
                                                            @AuthenticationPrincipal UserDetails userDetails) {
        return clubService.createClubSchedule(requestDto, clubId, userDetails.getUsername());
    }

    @PutMapping("/{clubId}/schedules/{scheduleId}")
    @ResponseStatus(HttpStatus.OK)
    public ClubScheduleEntityResponseDto updateClubSchedule(@PathVariable(name = "clubId") UUID clubId,
                                                            @Valid @RequestBody CreateOrUpdateClubScheduleRequestDto requestDto,
                                                            @PathVariable(name = "scheduleId") Long scheduleId,
                                                            @AuthenticationPrincipal UserDetails userDetails) {
        return clubService.updateClubSchedule(requestDto, clubId, scheduleId, userDetails.getUsername());
    }

    @DeleteMapping("/{clubId}/schedules/{scheduleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeClubSchedule(@PathVariable(name = "clubId") UUID clubId,
                                                            @PathVariable(name = "scheduleId") Long scheduleId,
                                                            @AuthenticationPrincipal UserDetails userDetails) {
        clubService.removeClubSchedule(clubId, scheduleId, userDetails.getUsername());
    }

    @DeleteMapping("/{clubId}/delete")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void removeClub(@PathVariable(name = "clubId") UUID clubId,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        clubService.removeClub(clubId, userDetails.getUsername());
    }

    @PutMapping("/{clubId}/members/{memberId}/role")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void changeMemberRole(@PathVariable(name = "clubId") UUID clubId,
                                 @PathVariable(name = "memberId") Long memberId,
                                 @Valid @RequestBody ChangeRoleRequestDto changeRoleDto,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        clubService.changeMemberRole(changeRoleDto, clubId, memberId, userDetails.getUsername());
    }

    @DeleteMapping("/{clubId}/members/{membersId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void removeMemberFromClub(@PathVariable(name = "clubId") UUID clubId,
                                     @PathVariable(name = "memberId") Long memberId,
                                     @AuthenticationPrincipal UserDetails userDetails) {
        clubService.removeMemberFromClub(clubId, memberId, userDetails.getUsername());
    }

    @PostMapping("/{clubId}/join")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ClubResponseDto joinClub(@PathVariable(name = "clubId") UUID clubId,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        ClubResponseDto clubResponseDto = clubService.addUserToClub(clubId, userDetails.getUsername());
        return clubResponseDto;
    }

    @DeleteMapping("/{clubId}/leave")
    @ResponseStatus(value = HttpStatus.OK)
    public ClubResponseDto leaveClub(@PathVariable(name = "clubId") UUID clubId,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        ClubResponseDto clubResponseDto = clubService.removeSelfFromClub(clubId, userDetails.getUsername());
        return clubResponseDto;
    }

    @PutMapping("/{clubId}/title-description")
    @ResponseStatus(HttpStatus.OK)
    public ClubResponseDto updateTitleAndDescription(@PathVariable(name = "clubId") UUID clubId,
                                                     @Valid @RequestBody UpdateTitleDescriptionRequestDto updateDto,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        return clubService.updateTitleAndDescription(clubId, updateDto, userDetails.getUsername());
    }


    @PutMapping("/{clubId}/profile-pic")
    @ResponseStatus(HttpStatus.OK)
    public ClubResponseDto updateProfilePic(@PathVariable(name = "clubId") UUID clubId,
                                            @Valid @RequestBody UploadImageRequestDto newImageDto,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        return clubService.updateProfilePic(clubId, newImageDto.getNewImageUrl(), userDetails.getUsername());
    }

    @PutMapping("/{clubId}/background-pic")
    @ResponseStatus(HttpStatus.OK)
    public ClubResponseDto updateBackgroundPic(@PathVariable(name = "clubId") UUID clubId,
                                            @Valid @RequestBody UploadImageRequestDto newImageDto,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        return clubService.updateBackgroundPic(clubId, newImageDto.getNewImageUrl(), userDetails.getUsername());
    }
}
