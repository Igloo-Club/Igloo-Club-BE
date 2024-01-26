package com.iglooclub.nungil.repository;

import com.iglooclub.nungil.domain.Acquaintance;
import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.domain.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Long> findRecommendingMemberIdList(Member member, List<Acquaintance> acquaintanceList){
        QMember qMember = QMember.member;

        List<Long> acquaintanceMemberIds = acquaintanceList.stream()
                .map(acquaintance -> acquaintance.getMember().getId())
                .collect(Collectors.toList());
        if(member.getDisableCompany().equals(true) && member.getPreferredAgeStart() != null){
            return queryFactory
                    .select(qMember.id)
                    .from(qMember)
                    .where(qMember.sex.ne(member.getSex()),
                            qMember.id.notIn(acquaintanceMemberIds),
                            qMember.company.ne(member.getCompany()),
                            qMember.birthdate.after(LocalDate.now().minusYears(member.getPreferredAgeEnd())),
                            qMember.birthdate.before(LocalDate.now().minusYears(member.getPreferredAgeStart())))
                    .fetch();
        }
        else if(member.getDisableCompany().equals(true) && member.getPreferredAgeStart() == null){
            return queryFactory
                    .select(qMember.id)
                    .from(qMember)
                    .where(qMember.sex.ne(member.getSex()),
                            qMember.id.notIn(acquaintanceMemberIds),
                            qMember.company.ne(member.getCompany()))
                    .fetch();
        }
        else if(member.getDisableCompany().equals(false) && member.getPreferredAgeStart() != null){
            return queryFactory
                    .select(qMember.id)
                    .from(qMember)
                    .where(qMember.sex.ne(member.getSex()),
                            qMember.id.notIn(acquaintanceMemberIds),
                            qMember.birthdate.after(LocalDate.now().minusYears(member.getPreferredAgeEnd())),
                            qMember.birthdate.before(LocalDate.now().minusYears(member.getPreferredAgeStart())))
                    .fetch();
        }
        else{
            return queryFactory
                    .select(qMember.id)
                    .from(qMember)
                    .where(qMember.sex.ne(member.getSex()),
                            qMember.id.notIn(acquaintanceMemberIds))
                    .fetch();
        }

    }




}
