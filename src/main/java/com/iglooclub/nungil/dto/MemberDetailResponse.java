package com.iglooclub.nungil.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iglooclub.nungil.domain.ConsentPolicy;
import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.domain.enums.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetailResponse {
    private String nickname;

    private Sex sex;

    @JsonFormat(pattern = "yyyyMMdd")
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

    private Boolean agreeMarketing;

    // == 생성 메서드 == //
    public static MemberDetailResponse create(Member member) {
        List<FaceDepiction> faceDepictionList = member.getFaceDepictionList();
        List<PersonalityDepiction> personalityDepictionList = member.getPersonalityDepictionList();
        List<Marker> markerList = member.getMarkerList();
        List<AvailableTime> availableTimeList = member.getAvailableTimeList();
        List<Hobby> hobbyList = member.getHobbyList();

        MemberDetailResponse response = new MemberDetailResponse();

        response.nickname = member.getNickname();
        response.sex = member.getSex();
        response.birthdate = member.getBirthdate();
        response.contactKakao = member.getContact().getKakao();
        response.contactInstagram = member.getContact().getInstagram();
        response.animalFace = member.getAnimalFace();
        response.job = member.getJob();
        response.height = member.getHeight();
        response.mbti = member.getMbti();
        response.marriageState = member.getMarriageState();
        response.religion = member.getReligion();
        response.alcohol = member.getAlcohol();
        response.smoke = member.getSmoke();
        response.faceDepictionList = faceDepictionList;
        response.personalityDepictionList = personalityDepictionList;
        response.description = member.getDescription();
        response.markerList = markerList;
        response.availableTimeList = availableTimeList;
        response.hobbyList = hobbyList;
        response.disableCompany = member.getDisableCompany();

        ConsentPolicy consentPolicy = member.getConsentPolicy();
        response.agreeMarketing = consentPolicy != null && consentPolicy.getAgreeMarketing();

        return response;
    }
}
