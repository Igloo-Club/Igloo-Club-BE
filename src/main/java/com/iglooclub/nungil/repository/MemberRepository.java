package com.iglooclub.nungil.repository;

import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.domain.OauthInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    Optional<Member> findByOauthInfo(OauthInfo oauthInfo);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByPhoneNumber(String phoneNumber);

    @Modifying
    @Query("update Member a set a.drawCount =0 where a.drawCount > 0")
    void initDrawCount();
}
