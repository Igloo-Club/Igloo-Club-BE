package com.iglooclub.nungil.controller;

import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.dto.MemberDetailResponse;
import com.iglooclub.nungil.dto.ProfileCreateRequest;
import com.iglooclub.nungil.dto.ProfileUpdateRequest;
import com.iglooclub.nungil.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/member")
    public ResponseEntity<?> createProfile(@RequestBody ProfileCreateRequest request, Principal principal) {
        Member member = getMember(principal);
        memberService.createProfile(member, request);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/api/member")
    public ResponseEntity<MemberDetailResponse> getMemberDetail(Principal principal) {
        Member member = getMember(principal);
        MemberDetailResponse memberDetail = memberService.getMemberDetail(member);
        return ResponseEntity.ok(memberDetail);
    }

    @PatchMapping("/api/member")
    public ResponseEntity<?> updateProfile(@RequestBody ProfileUpdateRequest request, Principal principal) {
        Member member = getMember(principal);
        memberService.updateProfile(member, request);
        return ResponseEntity.ok(null);
    }

    private Member getMember(Principal principal) {
        return memberService.findById(Long.parseLong(principal.getName()));
    }
}
