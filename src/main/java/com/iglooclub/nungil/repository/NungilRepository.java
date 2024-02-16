package com.iglooclub.nungil.repository;

import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.domain.Nungil;
import com.iglooclub.nungil.domain.enums.NungilStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NungilRepository extends JpaRepository<Nungil, Long> {
    Slice<Nungil> findAllByMemberAndStatus(PageRequest request, Member member, NungilStatus status);
    Optional<Nungil> findById(Long nungilId);
    Optional<Nungil> findFirstByMemberAndReceiver(Member member, Member receiver);
    List<Nungil> findAllByMemberAndReceiverAndStatus(Member member,Member receiver,NungilStatus status);

    @Modifying
    @Query("delete from Nungil n where n.status = :status")
    void deleteAllByStatus(@Param("status") NungilStatus status);

    @Modifying
    @Query("delete from Nungil n where n.expiredAt <= :dateTime")
    void deleteAllByExpiredAtBefore(@Param("dateTime") LocalDateTime dateTime);
}