package com.iglooclub.nungil.service;

import com.iglooclub.nungil.domain.Acquaintance;
import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.domain.Nungil;
import com.iglooclub.nungil.domain.enums.NungilStatus;
import com.iglooclub.nungil.dto.*;
import com.iglooclub.nungil.repository.AcquaintanceRepository;
import com.iglooclub.nungil.repository.MemberRepository;
import com.iglooclub.nungil.repository.NungilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
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
    public NungilResponse recommendMember(Principal principal, ProfileRecommendRequest request){
        Member member = getMember(principal);
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
     * @request NungilRequest 슬라이스 요청 정보
     * @return NungilPageResponse 슬라이스 정보 반환
     */
    public Slice<NungilSliceResponse> getNungilSliceByMemberAndStatus(Principal principal, NungilRequest request) {
        Member member = getMember(principal);
        PageRequest pageRequest = PageRequest.of(request.getPage(), request.getSize(),Sort.by("createdAt").descending());

        // Nungil 엔티티를 데이터베이스에서 조회
        Slice<Nungil> nungilSlice = nungilRepository.findAllByMemberAndStatus(pageRequest, member, request.getStatus());

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
                .faceDepictionAllocationList(member.getFaceDepictionAllocationList())
                .personalityDepictionAllocationList(member.getPersonalityDepictionAllocationList())
                .description(member.getDescription())
                .hobbyList(member.getHobbyList())
                .contact(member.getContact())
                .build();
    }
}