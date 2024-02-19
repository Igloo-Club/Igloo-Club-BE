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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

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

        member.updateProfile(request);
    }

    @Transactional
    public void updateSchedule(Member member, ScheduleUpdateRequest request) {
        // 가능한 요일 목록을 [일,월,...,토] 순으로 정렬
        List<Yoil> sortedYoilList = request.getYoilList();
        Collections.sort(sortedYoilList);

        member.updateSchedule(request.getLocation(), sortedYoilList, request.getAvailableTimeList(), request.getMarkerList());
    }

    /**
     * 인증번호를 생성하고, 주어진 전화번호로 인증문자를 전송하는 메서드이다.
     * @param request 인증 요청 DTO
     */
    public void sendAuthMessage(MessageAuthenticationRequest request, Member member) {

        String phoneNumber = request.getPhoneNumber();

        // 이미 가입된 전화번호인지 확인한다.
        checkDuplicatedPhoneNumber(phoneNumber, member);

        // code: 알파벳 대문자와 숫자로 구성된 랜덤 문자열의 인증번호
        String code = RandomStringUtil.numeric(6);
        String text = "[눈길] 인증번호 [" + code + "]를 입력해주세요";

        // redis에 전화번호를 키로 하여 인증번호를 저장한다.
        if (redisUtil.exists(phoneNumber)) {
            redisUtil.delete(phoneNumber);
        }
        redisUtil.set(phoneNumber, code, Duration.ofMinutes(5));

        // 인증문자를 발송한다.
        coolSMS.send(phoneNumber, text);
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
        checkDuplicatedPhoneNumber(phoneNumber, member);

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
     * 주어진 전화번호를 사용하는 회원이 존재하는지 확인하고, 다른 사람이 이미 사용한다면 예외를 발생시키는 메서드이다.
     * @param phoneNumber 회원 전화번호
     * @param requester 요청한 회원 엔티티
     */
    private void checkDuplicatedPhoneNumber(String phoneNumber, Member requester) {
        Optional<Member> optional = memberRepository.findByPhoneNumber(phoneNumber);
        // 주어진 전화번호를 사용하는 회원이 존재하지 않으면 종료
        if (optional.isEmpty()) {
            return;
        }

        // 주어진 전화번호를 사용하는 회원이 자기자신이면 종료
        Member member = optional.get();
        if (member.getId().equals(requester.getId())) {
            return;
        }

        throw new GeneralException(MemberErrorResult.DUPLICATED_PHONENUMBER);
    }

    /**
     * 주어진 회원의 '회사 사람 만나지 않기' 값을 변경하는 메서드이다.
     * @param member 값을 바꿀 Member 엔티티
     * @param disableCompany 바꿀 '회사 사람 만나지 않기' 값
     * @return 변경된 '회사 사람 만나지 않기' 값
     */
    @Transactional
    public DisableCompanyResponse updateDisableCompany(Member member, Boolean disableCompany) {
        Boolean newDisableCompany = member.updateDisableCompany(disableCompany);

        return DisableCompanyResponse.create(newDisableCompany);
    }

    /**
     * 매일 오후 3시에 drawCount = 0으로 초기화
     *
     */
    @Scheduled(cron = "0 0 15 * * *") // 매일 오후 3시에 실행
    @Transactional
    public void initMemberDrawCount() {
        memberRepository.initDrawCount();
    }

    /**
     * 주어진 회원의 location 필드를 수정하는 메서드이다.
     * @request locationRequest location 정보
     */
    @Transactional
    public void updateLocation(LocationRequest locationRequest, Member member){
        member.updateLocation(locationRequest.getLocation());
    }

    /**
     * 모든 마커 정보를 조회하는 메서드이다.
     * @param location 반환 마커 소재지
     */
    public List<AvailableMarker> getAllMarkers(Location location) {
        List<AvailableMarker> markerList = Arrays.asList(Marker.values()).stream()
                .filter(marker -> marker.getLocation() == location)
                .map(marker -> AvailableMarker.create(marker))
                .collect(Collectors.toList());
        return markerList;
    }

}
