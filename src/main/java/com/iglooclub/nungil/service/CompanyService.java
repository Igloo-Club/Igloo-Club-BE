package com.iglooclub.nungil.service;

import com.iglooclub.nungil.domain.Company;
import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.dto.CompanyListResponse;
import com.iglooclub.nungil.dto.CompanyVerificationRequest;
import com.iglooclub.nungil.exception.CompanyErrorResult;
import com.iglooclub.nungil.exception.GeneralException;
import com.iglooclub.nungil.exception.GlobalErrorResult;
import com.iglooclub.nungil.exception.MemberErrorResult;
import com.iglooclub.nungil.repository.CompanyRepository;
import com.iglooclub.nungil.repository.MemberRepository;
import com.iglooclub.nungil.util.EmailMessage;
import com.iglooclub.nungil.util.EmailSender;
import com.iglooclub.nungil.util.RandomStringUtil;
import com.iglooclub.nungil.util.StringRedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {
    private static final List<String> unavailableEmail = List.of("naver.com", "gmail.com", "daum.net", "kakao.com", "yahoo.com", "hanmail.net");

    private final CompanyRepository companyRepository;

    private final MemberRepository memberRepository;

    private final StringRedisUtil redisUtil;

    private final EmailSender emailSender;


    public CompanyListResponse getCompanyList(String email) {
        // 이메일에서 '@' 이후 부분만을 사용하여 탐색
        email = extractDomain(email);

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
     * 주어진 이메일에서 도메인('@' 이후 부분)을 추출하여 반환하는 메서드이다.
     * @param email 이메일 문자열
     * @return 도메인 문자열
     */
    private String extractDomain(String email) {
        return email.substring(1 + email.indexOf('@'));
    }

    /**
     * 인증번호를 생성하고, 주어진 회사 이메일로 발송하는 메서드이다.
     * @param email 회사 이메일
     */
    public void sendAuthEmail(String email) {

        // 사용이 불가능한 회사 도메인인지 확인한다.
        validateEmail(extractDomain(email));

        // 이미 가입된 이메일인지 확인한다.
        checkDuplicatedEmail(email);

        String subject = "[눈길] 회사 인증 메일입니다.";
        String filename = "company-authentication.html";

        // code: 알파벳 대문자와 숫자로 구성된 랜덤 문자열의 인증번호
        String code = RandomStringUtil.numeric(6);

        // 이메일을 발송한다.
        emailSender.send(EmailMessage.create(email, subject, filename).addContext("code", code));

        // redis에 이메일을 키로 하여 인증번호를 저장한다.
        if (redisUtil.exists(email)) {
            redisUtil.delete(email);
        }
        redisUtil.set(email, code, Duration.ofMinutes(5));
    }

    /**
     * 인증번호를 검증하고, 성공한 경우 회원 정보의 회사 정보를 수정하는 메서드이다.
     * @param request 인증번호 검증 요청 DTO
     * @param member 검증을 요청한 회원 엔티티
     */
    @Transactional
    public void verifyAuthCode(CompanyVerificationRequest request, Member member) {
        String email = request.getEmail();
        String companyName = request.getCompanyName();
        String companyDomain = extractDomain(request.getEmail());

        // 사용이 불가능한 회사 도메인인지 확인한다.
        validateEmail(companyDomain);

        // 이미 가입된 이메일인지 확인한다.
        checkDuplicatedEmail(email);

        // 요청된 회사 이메일을 키로 갖는 인증번호가 없거나 만료된 경우
        String foundCode = redisUtil.get(email);
        if (foundCode == null) {
            throw new GeneralException(GlobalErrorResult.REDIS_NOT_FOUND);
        }

        // 주어진 인증번호가 틀린 경우
        if (!foundCode.equals(request.getCode())) {
            throw new GeneralException(CompanyErrorResult.WRONG_AUTH_CODE);
        }

        // 회원 정보에 회사 정보를 추가한다.
        Company company = companyRepository.findByCompanyNameAndEmail(companyName, companyDomain)
                .orElse(Company.builder()
                        .companyName(companyName)
                        .email(companyDomain)
                        .build());
        companyRepository.save(company);

        member.setCompany(email, company);
    }

    /**
     * 주어진 email을 사용하는 회원이 존재하는지 확인하고, 이미 존재한다면 예외를 발생시키는 메서드이다.
     * @param email 회원 이메일
     */
    private void checkDuplicatedEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            throw new GeneralException(MemberErrorResult.DUPLICATED_EMAIL);
        }
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
