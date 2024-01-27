package com.iglooclub.nungil.service;

import com.iglooclub.nungil.domain.Company;
import com.iglooclub.nungil.dto.CompanyListResponse;
import com.iglooclub.nungil.exception.CompanyErrorResult;
import com.iglooclub.nungil.exception.GeneralException;
import com.iglooclub.nungil.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {

    private final CompanyRepository companyRepository;

    private static final List<String> unavailableEmail = List.of("naver.com", "gmail.com", "daum.net", "kakao.com", "yahoo.com", "hanmail.net");

    public CompanyListResponse getCompanyList(String email) {
        // 이메일에서 '@' 이후 부분만을 사용하여 탐색
        email = email.substring(1 + email.indexOf('@'));

        // 사용이 불가능한 회사 도메인이면 탐색을 막는다.
        validateEmail(email);

        List<Company> companyList = companyRepository.findTop8ByEmailOrderByCompanyNameAsc(email);
        List<String> companyNameList = companyList.stream()
                .map(Company::getCompanyName)
                .distinct()
                .collect(Collectors.toList());

        return CompanyListResponse.create(companyNameList, email);
    }

    /**
     * 사용이 불가능한 도메인인 경우, 예외를 발생시키는 메서드이다.
      * @param email 검사할 도메인
     */
    private void validateEmail(String email) {
        if (unavailableEmail.contains(email)) {
            throw new GeneralException(CompanyErrorResult.UNAVAILABLE_EMAIL);
        }
    }
}
