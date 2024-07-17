package com.runningmate.backend.club.service;

import com.runningmate.backend.club.Club;
import com.runningmate.backend.club.ClubMemberEntity;
import com.runningmate.backend.club.ClubRole;
import com.runningmate.backend.club.ClubScheduleEntity;
import com.runningmate.backend.club.dto.*;
import com.runningmate.backend.club.repository.ClubMemberEntityRepository;
import com.runningmate.backend.club.repository.ClubRepository;
import com.runningmate.backend.exception.BadRequestException;
import com.runningmate.backend.exception.ExistsConflictException;
import com.runningmate.backend.exception.NoPermissionException;
import com.runningmate.backend.exception.ResourceNotFoundException;
import com.runningmate.backend.member.Member;
import com.runningmate.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final ClubScheduleEntityService clubScheduleEntityService;
    private final ClubMemberEntityRepository clubMemberEntityRepository;
    private final MemberService memberService;

    @Transactional
    public ClubResponseDto createClub(ClubRequestDto clubRequestDto, String username) {
        //TODO: Discuss adding maximum clubs one can create
        Member member = memberService.getMemberByUsername(username);
        Club club = clubRepository.save(clubRequestDto.toEntity());
        ClubMemberEntity clubMemberEntity = clubMemberEntityRepository.save(createClubMemberEntity(club, member, ClubRole.OWNER));
        club.addMember(clubMemberEntity);

        return ClubResponseDto.fromEntity(club);
    }

    @Transactional
    public ClubResponseDto getClub(UUID clubId) {
        Club club = clubRepository.findById(clubId).orElseThrow(() -> new ResourceNotFoundException("Club not found"));
        return ClubResponseDto.fromEntity(club);
    }

    @Transactional
    public ListClubScheduleEntityResponseDto getClubSchedules(UUID clubId, String username) {
        Member member = memberService.getMemberByUsername(username);
        Club club = getClubById(clubId);
        //Check if the user is in the club
        clubMemberEntityRepository.findByClubAndMember(club, member)
                .orElseThrow(() -> new ResourceNotFoundException("You must first join the club to view club schedules"));

        List<ClubScheduleEntity> clubScheduleEntities = clubScheduleEntityService.getClubSchedules(clubId);
        List<ClubScheduleEntityResponseDto> clubSchedules = clubScheduleEntities.stream()
                .map(clubScheduleEntity -> ClubScheduleEntityResponseDto.fromEntity(clubScheduleEntity))
                .collect(Collectors.toList());
        return new ListClubScheduleEntityResponseDto(clubSchedules);
    }

    @Transactional
    public ClubScheduleEntityResponseDto createClubSchedule(CreateOrUpdateClubScheduleRequestDto createOrUpdateClubScheduleRequestDto,
                                                            UUID clubId,
                                                            String username) {
        Member member = memberService.getMemberByUsername(username);
        Club club = getClubById(clubId);
        //Check if the user is in the club
        ClubMemberEntity clubMemberEntity = clubMemberEntityRepository.findByClubAndMember(club, member)
                .orElseThrow(() -> new ResourceNotFoundException("You are not a member of this club"));
        //Check if user is the owner of the club
        if (clubMemberEntity.getRole() != ClubRole.OWNER && clubMemberEntity.getRole() != ClubRole.MODERATOR) {
            throw new NoPermissionException("You do not have permission to create a schedule at this club");
        }
        ClubScheduleEntity clubScheduleEntity = clubScheduleEntityService.createClubSchedule(club, createOrUpdateClubScheduleRequestDto);
        return ClubScheduleEntityResponseDto.fromEntity(clubScheduleEntity);
    }

    @Transactional
    public ClubScheduleEntityResponseDto updateClubSchedule(CreateOrUpdateClubScheduleRequestDto createOrUpdateClubScheduleRequestDto,
                                                            UUID clubId,
                                                            Long scheduleId,
                                                            String username) {
        ClubScheduleEntity clubScheduleEntity = clubScheduleEntityService.getClubScheduleById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
        Club club = clubScheduleEntity.getClub();
        //Check whether the schedule is club's
        if(!club.getId().equals(clubId)) {
            throw new ResourceNotFoundException("Schedule does not belong to the specified club");
        }
        Member member = memberService.getMemberByUsername(username);
        //Check if the user is in the club
        ClubMemberEntity clubMemberEntity = clubMemberEntityRepository.findByClubAndMember(club, member)
                .orElseThrow(() -> new ResourceNotFoundException("You are not a member of this club"));
        //Check if user is the owner of the club
        if (clubMemberEntity.getRole() != ClubRole.OWNER && clubMemberEntity.getRole() != ClubRole.MODERATOR) {
            throw new NoPermissionException("You do not have permission to create a schedule at this club");
        }

        clubScheduleEntity.update(createOrUpdateClubScheduleRequestDto);
        return ClubScheduleEntityResponseDto.fromEntity(clubScheduleEntity);
    }

    @Transactional
    public void removeClubSchedule(UUID clubId, Long scheduleId, String username) {
        ClubScheduleEntity clubScheduleEntity = clubScheduleEntityService.getClubScheduleById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
        Club club = clubScheduleEntity.getClub();
        //Check whether the schedule is club's
        if(!club.getId().equals(clubId)) {
            throw new ResourceNotFoundException("Schedule does not belong to the specified club");
        }
        Member member = memberService.getMemberByUsername(username);
        //Check if the user is in the club
        ClubMemberEntity clubMemberEntity = clubMemberEntityRepository.findByClubAndMember(club, member)
                .orElseThrow(() -> new ResourceNotFoundException("You are not a member of this club"));
        //Check if user is the owner of the club
        if (clubMemberEntity.getRole() != ClubRole.OWNER && clubMemberEntity.getRole() != ClubRole.MODERATOR) {
            throw new NoPermissionException("You do not have permission to create a schedule at this club");
        }
        clubScheduleEntityService.deleteClubSchedule(clubScheduleEntity.getId());
        club.removeClubSchedule(clubScheduleEntity);
    }

    @Transactional
    public void removeClub(UUID clubId, String username) {
        Member member = memberService.getMemberByUsername(username);
        //Check if club exists
        Club club = getClubById(clubId);
        //Check if the user is in the club
        ClubMemberEntity clubMemberEntity = clubMemberEntityRepository.findByClubAndMember(club, member)
                .orElseThrow(() -> new ResourceNotFoundException("Member is not in this club"));
        //Check if user is the owner of the club
        if (clubMemberEntity.getRole() != ClubRole.OWNER) {
            throw new NoPermissionException("Member does not have permission to delete the club");
        }

        clubRepository.delete(club);
    }

    @Transactional
    public void changeMemberRole(ChangeRoleRequestDto changeRoleRequestDto, UUID clubId, Long memberId, String username) {
        Member owner = memberService.getMemberByUsername(username);
        Member member = memberService.getMemberById(memberId);
        Club club = getClubById(clubId);
        //Check if requester is in the club
        ClubMemberEntity clubOwnerEntity = clubMemberEntityRepository.findByClubAndMember(club, owner)
                .orElseThrow(() -> new ResourceNotFoundException("You do not belong to this club"));
        //Check if request is the owner of the club
        if(clubOwnerEntity.getRole() != ClubRole.OWNER) {
            throw new NoPermissionException("You must be the owner to change roles");
        }
        //Check if member is in the club
        ClubMemberEntity clubMemberEntity = clubMemberEntityRepository.findByClubAndMember(club, member)
                .orElseThrow(() -> new ResourceNotFoundException("Member does not belong to this club"));

        clubMemberEntity.changeRole(changeRoleRequestDto.getClubRole());
    }

    @Transactional
    public void removeMemberFromClub(UUID clubId, Long memberId, String username) {
        Member owner = memberService.getMemberByUsername(username);
        Member member = memberService.getMemberById(memberId);
        Club club = getClubById(clubId);
        //Check if requester is in the club
        ClubMemberEntity clubOwnerEntity = clubMemberEntityRepository.findByClubAndMember(club, owner)
                .orElseThrow(() -> new NoPermissionException("You do not belong to this club"));
        //Check if request is the owner of the club
        if(clubOwnerEntity.getRole() != ClubRole.OWNER && clubOwnerEntity.getRole() != ClubRole.MODERATOR) {
            throw new NoPermissionException("You must be the owner to change roles");
        }
        //Check if member is in the club. If the member is not return without throwing an exception.
        Optional<ClubMemberEntity> clubMemberEntity = clubMemberEntityRepository.findByClubAndMember(club, member);
        if(clubMemberEntity.isEmpty()) {
            return;
        }
        //Can't remove owner
        if(clubMemberEntity.get().getRole() == ClubRole.OWNER) {
            throw new BadRequestException("You cannot remove owner of the club");
        }

        clubMemberEntityRepository.delete(clubMemberEntity.get());
    }

    @Transactional
    public ClubResponseDto addUserToClub(UUID clubId, String username) {
        Member member = memberService.getMemberByUsername(username);
        Club club = getClubById(clubId);
        if(clubMemberEntityRepository.findByClubAndMember(club, member).isPresent()) {
            throw new ExistsConflictException("Member is already in this club");
        }
        ClubMemberEntity clubMemberEntity = clubMemberEntityRepository.save(createClubMemberEntity(club, member, ClubRole.MEMBER));
        club.addMember(clubMemberEntity);

        return ClubResponseDto.fromEntity(club);
    }

    @Transactional
    public ClubResponseDto removeSelfFromClub(UUID clubId, String username) {
        Member member = memberService.getMemberByUsername(username);
        Club club = getClubById(clubId);
        //Check if requester is in the club
        ClubMemberEntity clubMemberEntity = clubMemberEntityRepository.findByClubAndMember(club, member)
                .orElseThrow(() -> new ResourceNotFoundException("You do not belong to this club"));
        if(clubMemberEntity.getRole() == ClubRole.OWNER) {
            throw new BadRequestException("You cannot remove owner of the club");
        }
        clubMemberEntityRepository.delete(clubMemberEntity);
        club.removeMember(clubMemberEntity);

        return ClubResponseDto.fromEntity(club);
    }

    @Transactional
    public ClubResponseDto updateTitleAndDescription(UUID clubId, UpdateTitleDescriptionRequestDto updateDto, String username) {
        Member member = memberService.getMemberByUsername(username);
        Club club = getClubById(clubId);
        //Check if requester is in the club
        ClubMemberEntity clubMemberEntity = clubMemberEntityRepository.findByClubAndMember(club, member)
                .orElseThrow(() -> new ResourceNotFoundException("You do not belong to this club"));
        //Check if requester is owner of the club
        if(clubMemberEntity.getRole() == ClubRole.OWNER) {
            throw new BadRequestException("You cannot remove owner of the club");
        }
        club.updateTitle(updateDto.getTitle());
        club.updateDescription(updateDto.getDescription());

        return ClubResponseDto.fromEntity(club);
    }

    @Transactional
    public ClubResponseDto updateProfilePic(UUID clubId, String url, String username) {
        Member member = memberService.getMemberByUsername(username);
        Club club = getClubById(clubId);
        ClubMemberEntity clubMemberEntity = clubMemberEntityRepository.findByClubAndMember(club, member)
                .orElseThrow(() -> new ResourceNotFoundException("This club does not have user " + username + " as member."));

        if (clubMemberEntity.getRole() != ClubRole.OWNER) {
            throw new IllegalArgumentException("Only the owner can update the profile picture");
        }

        club.updateProfilePic(url);
        return ClubResponseDto.fromEntity(clubRepository.save(club));
    }

    @Transactional
    public ClubResponseDto updateBackgroundPic(UUID clubId, String url, String username) {
        Member member = memberService.getMemberByUsername(username);
        Club club = getClubById(clubId);
        ClubMemberEntity clubMemberEntity = clubMemberEntityRepository.findByClubAndMember(club, member)
                .orElseThrow(() -> new ResourceNotFoundException("This club does not have user " + username + " as member."));

        if (clubMemberEntity.getRole() != ClubRole.OWNER) {
            throw new IllegalArgumentException("Only the owner can update the background picture");
        }

        club.updateBackgroundPic(url);
        return ClubResponseDto.fromEntity(clubRepository.save(club));
    }

    private ClubMemberEntity createClubMemberEntity(Club club, Member member, ClubRole clubRole) {
        return ClubMemberEntity.builder().club(club).member(member).role(clubRole).build();
    }



    @Transactional(readOnly = true)
    private Club getClubById(UUID clubId) {
        return clubRepository.findById(clubId).orElseThrow(() -> new ResourceNotFoundException("Club not found"));
    }
}
