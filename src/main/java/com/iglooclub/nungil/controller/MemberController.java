package com.iglooclub.nungil.controller;

import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.domain.enums.Location;
import com.iglooclub.nungil.dto.*;
import com.iglooclub.nungil.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/member")
    public ResponseEntity<?> createProfile(@RequestBody @Valid ProfileUpdateRequest request, Principal principal) {
        Member member = getMember(principal);
        memberService.updateProfile(member, request);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/api/member")
    public ResponseEntity<MemberDetailResponse> getMemberDetail(Principal principal) {
        Member member = getMember(principal);
        MemberDetailResponse memberDetail = memberService.getMemberDetail(member);
        return ResponseEntity.ok(memberDetail);
    }

    @PatchMapping("/api/member")
    public ResponseEntity<?> updateProfile(@RequestBody @Valid ProfileUpdateRequest request, Principal principal) {
        Member member = getMember(principal);
        memberService.updateProfile(member, request);
        return ResponseEntity.ok(null);
    }

    @PatchMapping("/api/member/company/toggle")
    public ResponseEntity<?> toggleDisableCompany(Principal principal) {
        Member member = getMember(principal);
        DisableCompanyResponse response = memberService.toggleDisableCompany(member);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/api/member/schedule")
    public ResponseEntity<?> updateSchedule(@RequestBody ScheduleUpdateRequest request, Principal principal) {
        Member member = getMember(principal);
        memberService.updateSchedule(member, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/member/phone/authentication")
    public ResponseEntity<?> sendAuthMessage(@RequestBody MessageAuthenticationRequest request, Principal principal) {
        Member member = getMember(principal);
        memberService.sendAuthMessage(request, member);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/member/phone/verification")
    public ResponseEntity<?> verifyAuthMessage(@RequestBody MessageVerificationRequest request, Principal principal) {
        Member member = getMember(principal);
        memberService.verifyAuthMessage(request, member);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/api/member/location")
    public ResponseEntity<?> updateLocation(@RequestBody LocationRequest request, Principal principal){
        Member member = getMember(principal);
        memberService.updateLocation(request,member);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/markers")
    public List<AvailableMarker> getAllMarkers(@RequestParam Location location){
        return memberService.getAllMarkers(location);
    }

    private Member getMember(Principal principal) {
        return memberService.findById(Long.parseLong(principal.getName()));
    }
}
