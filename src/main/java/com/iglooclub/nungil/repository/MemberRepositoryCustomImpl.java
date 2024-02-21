package com.iglooclub.nungil.repository;

import com.iglooclub.nungil.domain.Acquaintance;
import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.domain.QMember;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Long> findRecommendingMemberIdList(Member member, Boolean isPayed, List<Acquaintance> acquaintanceList){
        QMember qMember = QMember.member;

        List<Long> acquaintanceMemberIds = acquaintanceList.stream()
                .map(acquaintance -> acquaintance.getAcquaintanceMember().getId())
                .collect(Collectors.toList());

        JPAQuery<Long> query = queryFactory.select(qMember.id)
                .from(qMember);

        BooleanBuilder builder = nullSafeBuilder(() -> qMember.sex.ne(member.getSex()));

        //공통 where : 성별이 다른 member
        builder.and(qMember.sex.ne(member.getSex()));

        //공통 where : 지역이 같은 member
        builder.and(qMember.location.eq(member.getLocation()));

        //유료 여부 분기, 유료일 시 지인 추천 받지 않음
        if(isPayed){
            builder.and(qMember.id.notIn(acquaintanceMemberIds));
        }
        //회사사람 추천 받기 여부 분기
        if(member.getDisableCompany().equals(true)){
            builder.and(qMember.company.ne(member.getCompany()));
        }
        //선호나이 기입 여부 분기
        if(member.getPreferredAgeStart() != null){
            builder.and(qMember.birthdate.after(LocalDate.now().minusYears(member.getPreferredAgeEnd())))
                    .and(qMember.birthdate.before(LocalDate.now().minusYears(member.getPreferredAgeStart())));
        }

        return query.where(builder).fetch();

    }

    public static BooleanBuilder nullSafeBuilder(Supplier<BooleanExpression> f) {
        try {
            return new BooleanBuilder(f.get());
        } catch (IllegalArgumentException e) {
            return new BooleanBuilder();
        }
    }
}
