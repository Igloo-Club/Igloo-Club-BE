package com.iglooclub.nungil.service;

import com.iglooclub.nungil.domain.*;
import com.iglooclub.nungil.domain.enums.*;
import com.iglooclub.nungil.dto.*;
import com.iglooclub.nungil.exception.CompanyErrorResult;
import com.iglooclub.nungil.exception.GeneralException;
import com.iglooclub.nungil.exception.GlobalErrorResult;
import com.iglooclub.nungil.exception.MemberErrorResult;
import com.iglooclub.nungil.repository.*;
import com.iglooclub.nungil.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    private final FaceDepictionAllocationRepository faceDepictionAllocationRepository;

    private final PersonalityDepictionAllocationRepository personalityDepictionAllocationRepository;

    private final MarkerAllocationRepository markerAllocationRepository;

    private final HobbyAllocationRepository hobbyAllocationRepository;

    private final StringRedisUtil redisUtil;

    private final CoolSMS coolSMS;

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(MemberErrorResult.USER_NOT_FOUND));
    }

    /**
     * 주어진 회원의 프로필 정보를 등록하는 메서드이다.
     * @param member 프로필 정보가 등록되지 않은 회원
     * @param request 프로필 등록 요청 DTO
     */
    @Transactional
    public void createProfile(Member member, ProfileCreateRequest request) {
        // == 데이터베이스 테이블에 프로필 데이터가 있다면 전부 삭제한다. == //
        faceDepictionAllocationRepository.deleteAllByMember(member);
        personalityDepictionAllocationRepository.deleteAllByMember(member);
        markerAllocationRepository.deleteAllByMember(member);
        hobbyAllocationRepository.deleteAllByMember(member);

        member.createProfile(request);
    }

    /**
     * 회원 정보를 조회하는 메서드이다.
     * @param member 정보를 조회할 회원의 Member 클래스
     * @return 회원 정보 응답 DTO
     */
    public MemberDetailResponse getMemberDetail(Member member) {
        return MemberDetailResponse.create(member);
    }

    /**
     * 주어진 회원의 프로필 정보를 수정하는 메서드이다.
     * @param member 프로필 정보가 이미 등록된 회원
     * @param request 프로필 수정 요청 DTO
     */
    @Transactional
    public void updateProfile(Member member, ProfileUpdateRequest request) {

        // == 데이터베이스의 데이터들 중, 요청된 수정값이 아닌 값들을 모두 삭제한다. == //
        faceDepictionAllocationRepository.deleteAllByFaceDepictionNotIn(request.getFaceDepictionList());
        personalityDepictionAllocationRepository.deleteAllByPersonalityDepictionNotIn(request.getPersonalityDepictionList());
        hobbyAllocationRepository.deleteAllByHobbyNotIn(request.getHobbyList());

        // == 요청된 수정값들 중, 데이터베이스에 존재하지 않는 데이터들만을 삽입한다. == //
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

        List<HobbyAllocation> nonExistingHobbies = new ArrayList<>();
        for (Hobby hobby : request.getHobbyList()) {
            if (!member.getHobbyList().contains(hobby)) {
                nonExistingHobbies.add(HobbyAllocation.builder()
                        .hobby(hobby).member(member).build());
            }
        }

        member.updateProfile(request, nonExistingFaceDepictions, nonExistingPersonalityDepictions, nonExistingHobbies);
    }

    @Transactional
    public void updateSchedule(Member member, ScheduleUpdateRequest request) {
        // 가능한 요일 목록을 [일,월,...,토] 순으로 정렬
        List<Yoil> sortedYoilList = request.getYoilList();
        Collections.sort(sortedYoilList);

        member.updateSchedule(request.getLocation(), sortedYoilList, request.getAvailableTimeList());
    }

    /**
     * 인증번호를 생성하고, 주어진 전화번호로 인증문자를 전송하는 메서드이다.
     * @param request 인증 요청 DTO
     */
    public void sendAuthMessage(MessageAuthenticationRequest request) {

        String phoneNumber = request.getPhoneNumber();

        // 이미 가입된 이메일인지 확인한다.
        checkDuplicatedPhoneNumber(phoneNumber);

        // code: 알파벳 대문자와 숫자로 구성된 랜덤 문자열의 인증번호
        String code = RandomStringUtil.numeric(6);
        String text = "[눈길] 인증번호 [" + code + "]를 입력해주세요";

        // 이메일을 발송한다.
        coolSMS.send(phoneNumber, text);

        // redis에 이메일을 키로 하여 인증번호를 저장한다.
        if (redisUtil.exists(phoneNumber)) {
            redisUtil.delete(phoneNumber);
        }
        redisUtil.set(phoneNumber, code, Duration.ofMinutes(5));
    }

    /**
     * 인증번호를 검증하고, 성공한 경우 회원 정보의 전화번호 정보를 수정하는 메서드이다.
     * @param request 인증번호 검증 요청 DTO
     * @param member 검증을 요청한 회원 엔티티
     */
    @Transactional
    public void verifyAuthMessage(MessageVerificationRequest request, Member member) {
        String phoneNumber = request.getPhoneNumber();

        // 이미 가입된 이메일인지 확인한다.
        checkDuplicatedPhoneNumber(phoneNumber);

        // 요청된 전화번호를 키로 갖는 인증번호가 없거나 만료된 경우
        String foundCode = redisUtil.get(phoneNumber);
        if (foundCode == null) {
            throw new GeneralException(GlobalErrorResult.REDIS_NOT_FOUND);
        }

        // 주어진 인증번호가 틀린 경우
        if (!foundCode.equals(request.getCode())) {
            throw new GeneralException(CompanyErrorResult.WRONG_AUTH_CODE);
        }

        // 회원 정보에 전화번호 정보를 추가한다.
        member.updatePhoneNumber(phoneNumber);
    }

    /**
     * 주어진 전화번호를 사용하는 회원이 존재하는지 확인하고, 이미 존재한다면 예외를 발생시키는 메서드이다.
     * @param phoneNumber 회원 전화번호
     */
    private void checkDuplicatedPhoneNumber(String phoneNumber) {
        Optional<Member> member = memberRepository.findByPhoneNumber(phoneNumber);
        if (member.isPresent()) {
            throw new GeneralException(MemberErrorResult.DUPLICATED_PHONENUMBER);
        }
    }

    /**
     * 주어진 회원의 '회사 사람 만나지 않기' 값을 토글하는 메서드이다.
     * @param member 값을 토글할 회원 엔티티
     * @return 변경된 '회사 사람 만나지 않기' 값
     */
    @Transactional
    public DisableCompanyResponse toggleDisableCompany(Member member) {
        Boolean disableCompany = member.toggleDisableCompany();

        return DisableCompanyResponse.create(disableCompany);
    }

    /**
     * 주어진 회원의 location 필드를 수정하는 메서드이다.
     * @request locationRequest location 정보
     */
    @Transactional
    public void updateLocation(LocationRequest locationRequest, Member member){
        member.updateLocation(locationRequest.getLocation());
    }

}
