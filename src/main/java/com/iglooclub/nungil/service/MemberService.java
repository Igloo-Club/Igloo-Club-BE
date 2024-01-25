package com.iglooclub.nungil.service;

import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.exception.GeneralException;
import com.iglooclub.nungil.exception.MemberErrorResult;
import com.iglooclub.nungil.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(MemberErrorResult.USER_NOT_FOUND));
    }
}
