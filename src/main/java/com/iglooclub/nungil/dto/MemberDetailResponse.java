package com.iglooclub.nungil.dto;

import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.domain.enums.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class MemberDetailResponse {
    private String nickname;

    private String sex;

    private LocalDate birthdate;

    private String contactKakao;

    private String contactInstagram;

    private String animalFace;

    private String job;

    private Integer height;

    private Mbti mbti;

    private String marriageState;

    private String religion;

    private String alcohol;

    private String smoke;

    private List<String> faceDepictionList;

    private List<String> personalityDepictionList;

    private String description;

    private List<String> markerList;

    private List<String> availableTimeList;

    private List<String> hobbyList;

    private Boolean disableCompany;

    // == 생성 메서드 == //
    public static MemberDetailResponse create(Member member) {
        List<String> faceDepictionList = member.getFaceDepictionAllocationList().stream()
                .map(v -> v.getFaceDepiction().getTitle()).collect(Collectors.toList());
        List<String> personalityDepictionList = member.getPersonalityDepictionAllocationList().stream()
                .map(v -> v.getPersonalityDepiction().getTitle()).collect(Collectors.toList());
        List<String> markerList = member.getMarkerAllocationList().stream()
                .map(v -> v.getMarker().getTitle()).collect(Collectors.toList());
        List<String> availableTimeList = member.getAvailableTimeAllocationList().stream()
                .map(v -> v.getAvailableTime().getTitle()).collect(Collectors.toList());
        List<String> hobbyList = member.getHobbyAllocationList().stream()
                .map(v -> v.getHobby().getTitle()).collect(Collectors.toList());

        return MemberDetailResponse.builder()
                .nickname(member.getNickname())
                .sex(member.getSex().getTitle())
                .birthdate(member.getBirthdate())
                .contactKakao(member.getContact().getKakao())
                .contactInstagram(member.getContact().getInstagram())
                .animalFace(member.getAnimalFace().getTitle())
                .job(member.getJob())
                .height(member.getHeight())
                .mbti(member.getMbti())
                .marriageState(member.getMarriageState().getTitle())
                .religion(member.getReligion().getTitle())
                .alcohol(member.getAlcohol().getTitle())
                .smoke(member.getSmoke().getTitle())
                .faceDepictionList(faceDepictionList)
                .personalityDepictionList(personalityDepictionList)
                .description(member.getDescription())
                .markerList(markerList)
                .availableTimeList(availableTimeList)
                .hobbyList(hobbyList)
                .disableCompany(member.getDisableCompany())
                .build();
    }
}
