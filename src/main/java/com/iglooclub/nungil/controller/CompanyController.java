package com.iglooclub.nungil.controller;

import com.iglooclub.nungil.dto.CompanyListResponse;
import com.iglooclub.nungil.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
