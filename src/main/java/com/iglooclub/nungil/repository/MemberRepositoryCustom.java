package com.iglooclub.nungil.repository;

import com.iglooclub.nungil.domain.Acquaintance;
import com.iglooclub.nungil.domain.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Long> findRecommendingMemberIdList(Member member, List<Acquaintance> acquaintanceList);
}
