package com.iglooclub.nungil.service;

import com.iglooclub.nungil.domain.*;
import com.iglooclub.nungil.domain.enums.AvailableTime;
import com.iglooclub.nungil.domain.enums.Marker;
import com.iglooclub.nungil.domain.enums.NungilStatus;
import com.iglooclub.nungil.domain.enums.Yoil;
import com.iglooclub.nungil.dto.*;
import com.iglooclub.nungil.exception.GeneralException;
import com.iglooclub.nungil.exception.NungilErrorResult;
import com.iglooclub.nungil.repository.AcquaintanceRepository;
import com.iglooclub.nungil.repository.MemberRepository;
import com.iglooclub.nungil.repository.NungilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class NungilService {
    private final MemberRepository memberRepository;
    private final NungilRepository nungilRepository;
    private final AcquaintanceRepository acquaintanceRepository;

    private final MemberService memberService;

    /* 눈길 관리 */
    /**
     * 사용자를 추천하는 api입니다, 현재 분기는 3가지로 이루어집니다
     * isPayed : 유료 여부
     * disableCompany : 동일 회사 사람 추천 여부
     * 선호 연령대 기입 여부
     *
     * @request request 지불 여부 정보 (isPayed가 true일시 지불됨)
     * @return nungilResponse 추천되는 사용자 눈길 정보
     */
    @Transactional
    public NungilResponse recommendMember(Member member, ProfileRecommendRequest request){
        List<Acquaintance> acquaintanceList = acquaintanceRepository.findByMember(member);
        for(Acquaintance ac: acquaintanceList){
            System.out.println("지인 :"+ ac);
        }
        List<Long> recommendingMemberIdList = memberRepository.findRecommendingMemberIdList(member, request.getIsPayed(), acquaintanceList);

        if (recommendingMemberIdList.isEmpty()) {
            // 추천할 멤버가 없음. null 반환
            return null;
        }
        // 랜덤한 멤버 ID 선택
        Random random = new Random();
        Long recommendedMemberId = recommendingMemberIdList.get(random.nextInt(recommendingMemberIdList.size()));

        // 선택된 멤버 정보 가져오기
        Member recommendedMember = memberService.findById(recommendedMemberId);

        // Acquaintance 객체 생성 및 저장
        Acquaintance newAcquaintance = Acquaintance.builder()
                .member(member)
                .acquaintanceMember(recommendedMember)
                .build();

        acquaintanceRepository.save(newAcquaintance);

        Nungil newNungil = Nungil.create(member, recommendedMember, NungilStatus.RECOMMENDED);
        nungilRepository.save(newNungil);

        // 선택된 멤버 반환
        return convertToNungilResponse(recommendedMember);

    }
    /**
     * 요청 눈길상태의 프로필을 전체 조회하는 api입니다
     *
     * @param  page 페이지 정보
     * @param size 페이지 정보
     * @param status 요청 눈길 상태
     *
     * @return NungilPageResponse 슬라이스 정보 반환
     */
    public Slice<NungilSliceResponse> getNungilSliceByMemberAndStatus(Member member, NungilStatus status, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size,Sort.by("createdAt").descending());

        // Nungil 엔티티를 데이터베이스에서 조회
        Slice<Nungil> nungilSlice = nungilRepository.findAllByMemberAndStatus(pageRequest, member, status);

        // Nungil 엔티티를 NungilPageResponse DTO로 변환
        List<NungilSliceResponse> nungilResponses = nungilSlice.getContent().stream()
                .map(nungil -> NungilSliceResponse.builder()
                        .nungilId(nungil.getId())
                        .companyName(nungil.getMember().getCompany().getCompanyName()) // 이 부분은 Nungil 엔티티의 구조에 따라 달라질 수 있습니다.
                        .job(nungil.getMember().getJob())
                        .description(nungil.getReceiver().getDescription())
                        .createdAt(nungil.getCreatedAt())
                        .expiredAt(nungil.getExpiredAt())
                        .build())
                .collect(Collectors.toList());

        // 변환된 DTO 리스트와 함께 새로운 Slice 객체를 생성하여 반환
        return new SliceImpl<>(nungilResponses, pageRequest, nungilSlice.hasNext());
    }

    /**
     * 특정 눈길 정보를 조회하는 api입니다
     *
     * @param nungilId 눈길 id
     * @return nungilResponse 특정 눈길 정보
     */
    public NungilResponse getNungilDetail(Long nungilId){
        Nungil nungil = nungilRepository.findById(nungilId)
                .orElseThrow(() -> new GeneralException(NungilErrorResult.NUNGIL_NOT_FOUND));
        Member member = nungil.getReceiver();
        return convertToNungilResponse(member);
    }

    /**
     * 눈길을 보내는 api입니다
     * member에게 recommend status의 눈길을 SENT로 수정하며
     * receiver에게 status가 RECEIVED인 눈길을 생성합니다
     *
     * @param nungilId 눈길 id
     * @return nungilResponse 특정 눈길 정보
     */
    @Transactional
    public void sendNungil(Member member, Long nungilId){
        Nungil nungil = nungilRepository.findById(nungilId)
                .orElseThrow(()->new GeneralException(NungilErrorResult.NUNGIL_NOT_FOUND));
        Member receiver = nungil.getReceiver();

        if(!nungil.getStatus().equals(NungilStatus.RECOMMENDED)){
            throw new GeneralException(NungilErrorResult.NUNGIL_WRONG_STATUS);
        }

        //이미 눈길을 보냈을 시 중단
        List<Nungil> receiverNungilList = nungilRepository.findAllByMemberAndReceiverAndStatus(receiver, member, NungilStatus.RECEIVED);
        if(receiverNungilList.size() > 0){
            return ;
        }

        //사용자의 눈길 상태를 SENT, 만료일을 일주일 뒤로 설정
        nungil.setStatus(NungilStatus.SENT);
        nungil.setExpiredAt7DaysAfter();

        //눈길 받는 사용자 Acquaintance 객체 생성 및 저장
        List<Acquaintance> acquaintanceList = acquaintanceRepository.findByMember(receiver);

        Acquaintance newAcquaintance = Acquaintance.builder()
                .member(receiver)
                .acquaintanceMember(member)
                .build();

        acquaintanceRepository.save(newAcquaintance);

        //눈길 받는 사용자 눈길 객체 생성 및 저장
        Nungil newNungil = Nungil.create(receiver, member, NungilStatus.RECEIVED);
        newNungil.setExpiredAt7DaysAfter();
        nungilRepository.save(newNungil);
    }

    /**
     * 눈길을 매칭하는 api입니다
     *
     *
     * @param nungilId 눈길 id
     */
    @Transactional
    public void matchNungil(Long nungilId){
        Nungil receivedNungil = nungilRepository.findById(nungilId)
                .orElseThrow(()->new GeneralException(NungilErrorResult.NUNGIL_NOT_FOUND));

        //눈길이 잘못된 상태일 시 에러 발생
        if(!receivedNungil.getStatus().equals(NungilStatus.RECEIVED)){
            throw new GeneralException(NungilErrorResult.NUNGIL_WRONG_STATUS);
        }

        //사용자의 눈길을 MATCHED 상태로 변경
        receivedNungil.setStatus(NungilStatus.MATCHED);
        receivedNungil.setExpiredAtNull();

        //수취자의 눈길 조회 후 MATCHED 상태로 변경
        Member sender = receivedNungil.getReceiver();
        Optional<Nungil> optionalNungil = nungilRepository.findFirstByReceiver(sender);
        if(optionalNungil.isEmpty()){
            throw new GeneralException(NungilErrorResult.NUNGIL_NOT_FOUND);
        }
        Nungil sentNungil = optionalNungil.get();
        sentNungil.setStatus(NungilStatus.MATCHED);
        receivedNungil.setExpiredAtNull();
    }

    /**
     * 공통 매청 정보를 조회하는 api입니다
     *
     *
     * @param nungilId 눈길 id
     * @response nungilMatchResponse 눈길 매칭 정보
     */
    public NungilMatchResponse getMatchedNungil(Long nungilId){
        Member member = nungilRepository.findById(nungilId).get().getMember();
        Member receiver = nungilRepository.findById(nungilId).get().getReceiver();
        NungilMatchResponse response = NungilMatchResponse.builder()
                .yoil(findCommonYoil(member, receiver))
                .marker(findCommonMarkers(member, receiver).get(0))
                .time(findCommonAvailableTimes(member, receiver).get(0))
                .build();
        return response;
    }

    private Member getMember(Principal principal) {
        return memberService.findById(Long.parseLong(principal.getName()));
    }

    //Member 엔티티의 데이터를 NungilResponseDTO로 변환하는 메서드
    private NungilResponse convertToNungilResponse(Member member) {
        return NungilResponse.builder()
                .id(member.getId())
                .location(member.getLocation())
                .sex(member.getSex())
                .age(LocalDateTime.now().minusYears(member.getBirthdate().getYear()).getYear())
                .companyName(member.getCompany().getCompanyName())
                .nickname(member.getNickname())
                .animalFace(member.getAnimalFace())
                .alcohol(member.getAlcohol())
                .smoke(member.getSmoke())
                .religion(member.getReligion())
                .mbti(member.getMbti())
                .job(member.getJob())
                .height(member.getHeight())
                .marriageState(member.getMarriageState())
                .faceDepictionAllocationList(member.getFaceDepictionAllocationsAsString())
                .personalityDepictionAllocationList(member.getPersonalityDepictionAllocationAsString())
                .description(member.getDescription())
                .hobbyAllocationList(member.getHobbyAllocationAsString())
                .build();
    }
    //두 사용자의 공통 시간을 추출
    public List<AvailableTime> findCommonAvailableTimes(Member member1, Member member2) {
        List<AvailableTimeAllocation> list1 = member1.getAvailableTimeAllocationList();
        List<AvailableTimeAllocation> list2 = member2.getAvailableTimeAllocationList();

        Set<AvailableTime> timesSet1 = list1.stream()
                .map(AvailableTimeAllocation::getAvailableTime)
                .collect(Collectors.toSet());

        Set<AvailableTime> timesSet2 = list2.stream()
                .map(AvailableTimeAllocation::getAvailableTime)
                .collect(Collectors.toSet());

        // 교집합 찾기
        timesSet1.retainAll(timesSet2);

        return new ArrayList<>(timesSet1);
    }
    //두 사용자의 공통 마커를 추출
    public List<Marker> findCommonMarkers(Member member1, Member member2) {
        Set<Marker> markerSet1 = member1.getMarkerAllocationList().stream()
                .map(MarkerAllocation::getMarker)
                .collect(Collectors.toSet());

        Set<Marker> markerSet2 = member2.getMarkerAllocationList().stream()
                .map(MarkerAllocation::getMarker)
                .collect(Collectors.toSet());

        // 교집합 찾기
        markerSet1.retainAll(markerSet2);

        return new ArrayList<>(markerSet1);
    }

    public Yoil findCommonYoil(Member member1, Member member2) {
        List<Yoil> list1 = member1.getYoilList();
        List<Yoil> list2 = member2.getYoilList();
        Set<DayOfWeek> set1 = EnumSet.noneOf(DayOfWeek.class);
        for (Yoil yoil : list1) {
            set1.add(yoil.getDayOfWeek());
        }

        LocalDateTime now = LocalDateTime.now();
        boolean isAfter11AM = now.getHour() >= 11;

        for (Yoil yoil : list2) {
            if (set1.contains(yoil.getDayOfWeek())) {
                if (isAfter11AM) {
                    return findNextYoil(yoil, now);
                } else {
                    return yoil;
                }
            }
        }

        return null;
    }

    private Yoil findNextYoil(Yoil yoil, LocalDateTime now) {
        DayOfWeek today = now.getDayOfWeek();
        int daysUntilNext = (7 + yoil.getDayOfWeek().getValue() - today.getValue()) % 7;
        if (daysUntilNext == 0) {
            daysUntilNext = 7;
        }
        return Yoil.values()[(today.getValue() + daysUntilNext - 1) % 7];
    }
}