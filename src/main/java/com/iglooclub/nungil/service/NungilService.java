package com.iglooclub.nungil.service;

import com.iglooclub.nungil.domain.*;
import com.iglooclub.nungil.domain.enums.*;
import com.iglooclub.nungil.dto.*;
import com.iglooclub.nungil.exception.ChatRoomErrorResult;
import com.iglooclub.nungil.exception.GeneralException;
import com.iglooclub.nungil.exception.MemberErrorResult;
import com.iglooclub.nungil.exception.NungilErrorResult;
import com.iglooclub.nungil.repository.AcquaintanceRepository;
import com.iglooclub.nungil.repository.ChatRoomRepository;
import com.iglooclub.nungil.repository.MemberRepository;
import com.iglooclub.nungil.repository.NungilRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.DayOfWeek;
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

    private final ChatRoomRepository chatRoomRepository;

    private final MemberService memberService;

    private static final Long RECOMMENDATION_LIMIT = 3L;

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

        // 1. 하루 제한 횟수를 초과한 경우, 예외를 발생시킨다.
        if (checkLimitExcess(member)) {
            throw new GeneralException(NungilErrorResult.LIMIT_EXCEEDED);
        }

        // 2. 회원 한 명을 추천받는다.
        Member recommendedMember = getRecommendedMember(member, request);
        if (recommendedMember == null) return null;

        // 3. 추천 받은 회원에 대한 지인 관계를 생성하고 저장한다.
        Acquaintance newAcquaintance1 = getAcquaintance(member, recommendedMember);
        Acquaintance newAcquaintance2 = getAcquaintance(recommendedMember, member);
        acquaintanceRepository.save(newAcquaintance1);
        acquaintanceRepository.save(newAcquaintance2);

        // 4. 추천 받은 회원에 대한 눈길을 생성하고 저장한다.
        Nungil newNungil = Nungil.create(member, recommendedMember, NungilStatus.RECOMMENDED);
        nungilRepository.save(newNungil);

        // 5. 추천이 정상적으로 동작했을 시 drawCount를 1 증가 시킨다.
        member.plusDrawCount();

        // 6. 추천 받은 회원 정보를 반환한다.
        return convertToNungilResponse(recommendedMember);
    }

    @Nullable
    private Member getRecommendedMember(Member member, ProfileRecommendRequest request) {
        List<Acquaintance> acquaintanceList = acquaintanceRepository.findByMember(member);
        List<Long> recommendingMemberIdList = memberRepository.findRecommendingMemberIdList(member, request.getIsPayed(), acquaintanceList);

        if (recommendingMemberIdList.isEmpty()) {
            // 추천할 멤버가 없음. null 반환
            return null;
        }
        // 랜덤한 멤버 ID 선택
        Random random = new Random();
        Long recommendedMemberId = recommendingMemberIdList.get(random.nextInt(recommendingMemberIdList.size()));

        // 선택된 멤버 정보 가져오기
        return memberService.findById(recommendedMemberId);
    }

    /**
     * 사용자 추천 제한 횟수를 초과했는지 확인하는 메서드이다.
     * @param member 제한 횟수 초과를 확인할 회원
     * @return 초과한 경우 true, 제한 횟수가 남은 경우 false
     */
    private boolean checkLimitExcess(Member member) {
        Long count = member.getDrawCount();
        return RECOMMENDATION_LIMIT <= count;
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
                .map(nungil -> NungilSliceResponse.create(nungil, nungil.getReceiver()))
                .collect(Collectors.toList());



        // 변환된 DTO 리스트와 함께 새로운 Slice 객체를 생성하여 반환
        return new SliceImpl<>(nungilResponses, pageRequest, nungilSlice.hasNext());
    }

    /**
     * 추천 눈길상태의 프로필을 전체 조회하는 api입니다. 오늘 프로필을 추천받지 않았다면, 하나 뽑습니다.
     *
     * @param  page 페이지 정보
     * @param size 페이지 정보
     *
     * @return NungilPageResponse 슬라이스 정보 반환
     */
    @Transactional
    public Slice<NungilSliceResponse> getRecommendedNungilSlice(Member member, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size,Sort.by("createdAt").descending());

        // 사용자가 아직까지 프로필을 뽑지 않았다면, 자동으로 하나 뽑는다.
        if (member.getDrawCount() == 0) {
            this.recommendMember(member, new ProfileRecommendRequest(true));
        }

        // Nungil 엔티티를 데이터베이스에서 조회
        Slice<Nungil> nungilSlice = nungilRepository.findAllByMemberAndStatus(pageRequest, member, NungilStatus.RECOMMENDED);

        if (member.getLocation() == null || nungilSlice.getContent().stream()
                .anyMatch(nungil -> nungil.getReceiver().getLocation() == null)) {
            throw new GeneralException(MemberErrorResult.LOCATION_NOT_FOUND);
        }

        // Nungil 엔티티를 NungilPageResponse DTO로 변환
        List<NungilSliceResponse> nungilResponses = nungilSlice.getContent().stream()
                .filter(nungil -> member.getLocation().equals(nungil.getReceiver().getLocation()))
                .map(nungil -> NungilSliceResponse.create(nungil, nungil.getReceiver()))
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
     */
    @Transactional
    public void sendNungil(Member member, Long nungilId){
        Nungil nungil = nungilRepository.findById(nungilId)
                .orElseThrow(()->new GeneralException(NungilErrorResult.NUNGIL_NOT_FOUND));
        Member receiver = nungil.getReceiver();
        Acquaintance memberAcquaintance = getAcquaintance(member, receiver);
        Acquaintance receiverAcquaintance = getAcquaintance(receiver, member);

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
        memberAcquaintance.update(NungilStatus.SENT);


        //눈길 받는 사용자 눈길 객체 생성 및 저장
        Nungil newNungil = Nungil.create(receiver, member, NungilStatus.RECEIVED);
        newNungil.setExpiredAt7DaysAfter();
        receiverAcquaintance.update(NungilStatus.RECEIVED);
        acquaintanceRepository.save(receiverAcquaintance);
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
        Member member = receivedNungil.getMember();
        Member sender = receivedNungil.getReceiver();
        Optional<Nungil> optionalNungil = nungilRepository.findFirstByMemberAndReceiver(sender, member);
        if(optionalNungil.isEmpty()){
            throw new GeneralException(NungilErrorResult.NUNGIL_NOT_FOUND);
        }
        Nungil sentNungil = optionalNungil.get();
        sentNungil.setStatus(NungilStatus.MATCHED);
        sentNungil.setExpiredAtNull();

        // 서로에 대한 Acquaintance 객체 생성 및 저장
        Acquaintance acquaintanceFromMember = getAcquaintance(member, sender);
        acquaintanceFromMember.update(NungilStatus.MATCHED);
        acquaintanceRepository.save(acquaintanceFromMember);

        Acquaintance acquaintanceFromSender = getAcquaintance(sender, member);
        acquaintanceFromSender.update(NungilStatus.MATCHED);
        acquaintanceRepository.save(acquaintanceFromSender);

        // 매칭된 사용자 간에 겹치는 시간, 마커를 조회하여 저장
        List<Marker> marker = findCommonMarkers(member, sender);
        Yoil yoil = findCommonYoil(member, sender);
        AvailableTime time = null;

        List<AvailableTime> commonAvailableTimes = findCommonAvailableTimes(member, sender);
        if(!commonAvailableTimes.isEmpty()){
            time = commonAvailableTimes.get(0);
        }
        receivedNungil.update(marker, time, yoil);
        sentNungil.update(marker, time, yoil);

        // 매칭된 사용자들을 채팅방에 초대
        ChatRoom chatRoom = ChatRoom.create(member, sender);
        chatRoomRepository.save(chatRoom);
    }

    /**
     * 공통 매칭 정보를 조회하는 api입니다
     * @param nungilId 눈길 id
     * @response nungilMatchResponse 눈길 매칭 정보
     */
    public NungilMatchResponse getMatchedNungil(Long nungilId, Member member){

        Nungil nungil = nungilRepository.findById(nungilId)
                .orElseThrow(() -> new GeneralException(NungilErrorResult.NUNGIL_NOT_FOUND));

        // 요청을 보낸 사용자가 주어진 눈길의 소유자인지 확인한다.
        if (!nungil.getMember().equals(member)) {
            throw new GeneralException(MemberErrorResult.NOT_OWNER);
        }

        List<ChatRoom> chatRooms = chatRoomRepository.findByMembers(nungil.getMember(), nungil.getReceiver());

        // chat room 2개 이상일 경우 예외 처리
        if (chatRooms.size() > 1) {
            throw new GeneralException(ChatRoomErrorResult.CHAT_ROOM_MORE_THAN_ONE);
        }

        // 두 사용자가 속한 채팅방이 존재하지 않는 경우 null을 반환한다.
        Long chatRoomId = chatRooms.isEmpty() ? null : chatRooms.get(0).getId();


        return NungilMatchResponse.create(nungil, chatRoomId);
    }

    /**
     * 매 시간 expireAt이 초과된 눈길을 삭제합니다
     *
     */
    @Scheduled(cron = "0 0 * * * *") // 매시 정각에 실행
    @Transactional
    public void deleteExpiredNungils() {
        LocalDateTime now = LocalDateTime.now();
        nungilRepository.deleteAllByExpiredAtBefore(now);
        acquaintanceRepository.deleteAllByExpiredAtBefore(now);
    }

    /**
     * 매일 오전 11시에 추천된 눈길을 삭제하고,
     * 전날에 추천되었던 회원이 다시 추천될 수 있도록 합니다.
     *
     */
    @Scheduled(cron = "0 0 11 * * *") // 매일 오전 11시에 실행
    @Transactional
    public void deleteRecommendedNungils() {
        nungilRepository.deleteAllByStatus(NungilStatus.RECOMMENDED);
        acquaintanceRepository.deleteAllByStatus(NungilStatus.RECOMMENDED);
    }

    private Member getMember(Principal principal) {
        return memberService.findById(Long.parseLong(principal.getName()));
    }

    //Member 엔티티의 데이터를 NungilResponseDTO로 변환하는 메서드
    private NungilResponse convertToNungilResponse(Member member) {
        return NungilResponse.builder()
                .id(member.getId())
                .location(member.getLocation().getTitle())
                .sex(member.getSex())
                .age(LocalDateTime.now().minusYears(member.getBirthdate().getYear()).getYear())
                .companyName(member.getCompany().getCompanyName())
                .nickname(member.getNickname())
                .animalFace(member.getAnimalFace().getTitle())
                .alcohol(member.getAlcohol().getTitle())
                .smoke(member.getSmoke().getTitle())
                .religion(member.getReligion().getTitle())
                .mbti(member.getMbti())
                .job(member.getJob())
                .height(member.getHeight())
                .marriageState(member.getMarriageState().getTitle())
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
                    return findNextYoil(yoil, now, set1);
                } else {
                    return yoil;
                }
            }
        }

        return null;
    }

    private Yoil findNextYoil(Yoil yoil, LocalDateTime now, Set<DayOfWeek> availableDays) {
        DayOfWeek today = now.getDayOfWeek();
        int offset = 0;
        DayOfWeek nextDay = today;

        do {
            offset++;
            nextDay = DayOfWeek.of((today.getValue() + offset - 1) % 7 + 1); // 요일을 순환합니다.
        } while (!availableDays.contains(nextDay)); // 다음 사용 가능한 요일을 찾을 때까지 반복

        for (Yoil nextYoil : Yoil.values()) {
            if (nextYoil.getDayOfWeek().equals(nextDay)) {
                return nextYoil; // 다음 요일에 해당하는 Yoil 열거형을 반환
            }
        }

        return yoil;
    }

    /**
     * ACQUAINTANCE 데이터베이스에 member와 acquaintanceMember로 구성된 데이터가 없는 경우 생성하고, 있는 경우 조회하여 반환하는 메서드이다.
     * @param member Member 엔티티
     * @param acquaintanceMember member 회원의 지인을 가리키는 Member 엔티티
     * @return 조회된 Acquaintance 엔티티
     */
    private Acquaintance getAcquaintance(Member member, Member acquaintanceMember) {
        return acquaintanceRepository.findByMemberAndAcquaintanceMember(member, acquaintanceMember)
                .orElse(Acquaintance.create(member, acquaintanceMember, NungilStatus.RECOMMENDED));
    }
}