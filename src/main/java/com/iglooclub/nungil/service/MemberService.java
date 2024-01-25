package com.iglooclub.nungil.service;

import com.iglooclub.nungil.domain.*;
import com.iglooclub.nungil.domain.enums.*;
import com.iglooclub.nungil.dto.MemberDetailResponse;
import com.iglooclub.nungil.dto.ProfileCreateRequest;
import com.iglooclub.nungil.dto.ProfileUpdateRequest;
import com.iglooclub.nungil.exception.GeneralException;
import com.iglooclub.nungil.exception.MemberErrorResult;
import com.iglooclub.nungil.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    private final FaceDepictionAllocationRepository faceDepictionAllocationRepository;

    private final PersonalityDepictionAllocationRepository personalityDepictionAllocationRepository;

    private final MarkerAllocationRepository markerAllocationRepository;

    private final AvailableTimeAllocationRepository availableTimeAllocationRepository;

    private final HobbyAllocationRepository hobbyAllocationRepository;

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(MemberErrorResult.USER_NOT_FOUND));
    }

    @Transactional
    public void createProfile(Member member, ProfileCreateRequest request) {
        member.createProfile(request);
    }

    public MemberDetailResponse getMemberDetail(Member member) {
        return MemberDetailResponse.create(member);
    }

    @Transactional
    public void updateProfile(Member member, ProfileUpdateRequest request) {

        // 데이터베이스의 데이터들 중, 요청된 수정값이 아닌 값들을 모두 삭제한다. //
        faceDepictionAllocationRepository.deleteAllByFaceDepictionNotIn(request.getFaceDepictionList());
        personalityDepictionAllocationRepository.deleteAllByPersonalityDepictionNotIn(request.getPersonalityDepictionList());
        markerAllocationRepository.deleteAllByMarkerNotIn(request.getMarkerList());
        availableTimeAllocationRepository.deleteAllByAvailableTimeNotIn(request.getAvailableTimeList());
        hobbyAllocationRepository.deleteAllByHobbyNotIn(request.getHobbyList());

        // 요청된 수정값들 중, 데이터베이스에 존재하지 않는 데이터들만을 삽입한다. //
        List<FaceDepictionAllocation> nonExistingFaceDepictions = new ArrayList<>();
        for (FaceDepiction faceDepiction : request.getFaceDepictionList()) {
            if (!member.getFaceDepictionList().contains(faceDepiction)) {
                nonExistingFaceDepictions.add(FaceDepictionAllocation.builder()
                        .faceDepiction(faceDepiction).member(member).build());
            }
        }

        List<PersonalityDepictionAllocation> nonExistingPersonalityDepictions = new ArrayList<>();
        for (PersonalityDepiction personalityDepiction : request.getPersonalityDepictionList()) {
            if (!member.getPersonalityDepictionList().contains(personalityDepiction)) {
                nonExistingPersonalityDepictions.add(PersonalityDepictionAllocation.builder()
                        .personalityDepiction(personalityDepiction).member(member).build());
            }
        }

        List<MarkerAllocation> nonExistingMarkers = new ArrayList<>();
        for (Marker marker : request.getMarkerList()) {
            if (!member.getMarkerList().contains(marker)) {
                nonExistingMarkers.add(MarkerAllocation.builder()
                        .marker(marker).member(member).build());
            }
        }

        List<AvailableTimeAllocation> nonExistingAvailableTimes = new ArrayList<>();
        for (AvailableTime availableTime : request.getAvailableTimeList()) {
            if (!member.getAvailableTimeList().contains(availableTime)) {
                nonExistingAvailableTimes.add(AvailableTimeAllocation.builder()
                        .availableTime(availableTime).member(member).build());
            }
        }

        List<HobbyAllocation> nonExistingHobbies = new ArrayList<>();
        for (Hobby hobby : request.getHobbyList()) {
            if (!member.getHobbyList().contains(hobby)) {
                nonExistingHobbies.add(HobbyAllocation.builder()
                        .hobby(hobby).member(member).build());
            }
        }

        member.updateProfile(request, nonExistingFaceDepictions, nonExistingPersonalityDepictions, nonExistingMarkers, nonExistingAvailableTimes, nonExistingHobbies);
    }
}
