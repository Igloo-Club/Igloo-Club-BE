package com.iglooclub.nungil.controller;

import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.dto.NungilResponse;
import com.iglooclub.nungil.dto.ProfileRecommendRequest;
import com.iglooclub.nungil.service.NungilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/nungil")
@RequiredArgsConstructor
public class NungilController {
    private final NungilService nungilService;
    @GetMapping("/recommend")
    public ResponseEntity<NungilResponse> recommendMember( Principal principal, @RequestBody ProfileRecommendRequest request){
        Member recommendedMember = nungilService.recommendMember(principal, request);
        if (recommendedMember == null){
            //추천할 사용자가 없는 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        NungilResponse nungilResponse = convertToNungilResponse(recommendedMember);
        return ResponseEntity.ok(nungilResponse);
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
