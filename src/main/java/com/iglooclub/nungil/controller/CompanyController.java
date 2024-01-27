package com.iglooclub.nungil.controller;

import com.iglooclub.nungil.dto.CompanyEmailRequest;
import com.iglooclub.nungil.dto.CompanyListResponse;
import com.iglooclub.nungil.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/company")
public class CompanyController {

    private final CompanyService companyService;

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
}
