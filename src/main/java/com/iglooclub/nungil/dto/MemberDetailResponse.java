package com.iglooclub.nungil.dto;

import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.domain.enums.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class MemberDetailResponse {
    private String nickname;

    private Sex sex;

    private LocalDate birthdate;

    private String contactKakao;

    private String contactInstagram;

    private AnimalFace animalFace;

    private String job;

    private Integer height;

    private Mbti mbti;

    private MarriageState marriageState;

    private Religion religion;

    private Alcohol alcohol;

    private Smoke smoke;

    private List<FaceDepiction> faceDepictionList;

    private List<PersonalityDepiction> personalityDepictionList;

    private String description;

    private List<Marker> markerList;

    private List<AvailableTime> availableTimeList;

    private List<Hobby> hobbyList;

    private Boolean disableCompany;

    // == 생성 메서드 == //
    public static MemberDetailResponse create(Member member) {
        List<FaceDepiction> faceDepictionList = member.getFaceDepictionList();
        List<PersonalityDepiction> personalityDepictionList = member.getPersonalityDepictionList();
        List<Marker> markerList = member.getMarkerList();
        List<AvailableTime> availableTimeList = member.getAvailableTimeList();
        List<Hobby> hobbyList = member.getHobbyList();

        return MemberDetailResponse.builder()
                .nickname(member.getNickname())
                .sex(member.getSex())
                .birthdate(member.getBirthdate())
                .contactKakao(member.getContact().getKakao())
                .contactInstagram(member.getContact().getInstagram())
                .animalFace(member.getAnimalFace())
                .job(member.getJob())
                .height(member.getHeight())
                .mbti(member.getMbti())
                .marriageState(member.getMarriageState())
                .religion(member.getReligion())
                .alcohol(member.getAlcohol())
                .smoke(member.getSmoke())
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
