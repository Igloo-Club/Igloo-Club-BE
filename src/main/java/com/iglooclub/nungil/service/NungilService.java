package com.iglooclub.nungil.service;

import com.iglooclub.nungil.domain.Acquaintance;
import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.repository.AcquaintanceRepository;
import com.iglooclub.nungil.repository.MemberRepository;
import com.iglooclub.nungil.repository.NungilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Random;

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
     * 사용자를 추천하는 api입니다, 현재 추천은 4가지로 이루어집니다
     * 회사 비활성화 & 선호 연령대 지정 & 랜덤
     * 회사 비활성화 & 선호 연령대 미지정 & 랜덤
     * 회사 활성화 & 선호 연령대 지정 & 랜덤
     * 회사 활성화 & 선호 연령대 미지정 & 랜덤
     *
     * @return recommendMember 추천되는 사용자 객체
     */
    @Transactional
    public Member recommendMember(Principal principal){
        Member member = getMember(principal);
        List<Acquaintance> acquaintanceList = acquaintanceRepository.findByMember(member);
        List<Long> recommendingMemberIdList = memberRepository.findRecommendingMemberIdList(member, acquaintanceList);

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

        // 선택된 멤버 반환
        return recommendedMember;

    }

    private Member getMember(Principal principal) {
        return memberService.findById(Long.parseLong(principal.getName()));
    }

}