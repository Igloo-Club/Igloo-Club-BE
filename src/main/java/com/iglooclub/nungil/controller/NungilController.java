package com.iglooclub.nungil.controller;

import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.domain.enums.NungilStatus;
import com.iglooclub.nungil.dto.*;
import com.iglooclub.nungil.service.MemberService;
import com.iglooclub.nungil.service.NungilService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/nungil")
@RequiredArgsConstructor
public class NungilController {
    private final NungilService nungilService;
    private final MemberService memberService;
    @PostMapping("/recommend")
    public ResponseEntity<NungilResponse> recommendMember( Principal principal, @RequestBody ProfileRecommendRequest request){
        Member member = getMember(principal);
        NungilResponse nungilResponse = nungilService.recommendMember(member, request);
        if (nungilResponse == null){
            //추천할 사용자가 없는 경우
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(nungilResponse);
    }

    @GetMapping("/nungils")
    public ResponseEntity<Slice<NungilSliceResponse>> getNungilsByMemberAndStatus(Principal principal, @RequestParam NungilStatus status, @RequestParam int page, @RequestParam int size){
        Member member = getMember(principal);
        Slice<NungilSliceResponse> nungilPageResponses = nungilService.getNungilSliceByMemberAndStatus(member, status, page, size);
        return ResponseEntity.ok(nungilPageResponses);
    }


    @GetMapping("/detail")
    public ResponseEntity<NungilResponse> getNungilDetail(Principal principal, @RequestParam Long nungilId){
        NungilResponse nungilResponse = nungilService.getNungilDetail(nungilId);
        return ResponseEntity.ok(nungilResponse);
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendNungil(Principal principal, @RequestParam Long nungilId){
        Member member = getMember(principal);
        nungilService.sendNungil(member, nungilId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/match")
    public ResponseEntity<?> matchNungil(@RequestParam Long nungilId){
        nungilService.matchNungil(nungilId);
        return new ResponseEntity<>(HttpStatus.OK);
    }



    private Member getMember(Principal principal) {
        return memberService.findById(Long.parseLong(principal.getName()));
    }

}
