package com.iglooclub.nungil.controller;

import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.dto.CompanyEmailRequest;
import com.iglooclub.nungil.dto.CompanyListResponse;
import com.iglooclub.nungil.dto.CompanyVerificationRequest;
import com.iglooclub.nungil.service.CompanyService;
import com.iglooclub.nungil.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/company")
public class CompanyController {

    private final CompanyService companyService;

    private final MemberService memberService;

    /**
     * 주어진 이메일을 사용하는 회사명 목록을 반환하는 메서드이다.
     * @param email 회사 이메일
     * @return 회사명 목록
     */
    @GetMapping
    public ResponseEntity<CompanyListResponse> getCompanyList(@RequestParam String email) {
        CompanyListResponse companyList = companyService.getCompanyList(email);
        return ResponseEntity.ok(companyList);
    }

    /**
     * 주어진 회사 이메일로 인증 확인 메일을 보내는 메서드이다.
     * @param request 회사 이메일이 포함된 DTO
     */
    @PostMapping("/email")
    public ResponseEntity<?> sendAuthEmail(@RequestBody CompanyEmailRequest request) {
        companyService.sendAuthEmail(request.getEmail());

        return ResponseEntity.ok(null);
    }

    /**
     * 인증번호를 검증하고, 성공 시 회사 정보를 저장하는 메서드이다.
     * @param request 인증번호 검증 요청 DTO
     * @param principal 회원 정보가 담긴 Principal 객체
     */
    @PostMapping("/verification")
    public ResponseEntity<?> verifyAuthCode(@RequestBody CompanyVerificationRequest request, Principal principal) {
        Member member = memberService.findById(Long.parseLong(principal.getName()));

        companyService.verifyAuthCode(request, member);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
