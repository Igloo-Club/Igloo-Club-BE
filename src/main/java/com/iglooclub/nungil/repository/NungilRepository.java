package com.iglooclub.nungil.repository;

import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.domain.Nungil;
import com.iglooclub.nungil.domain.enums.NungilStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NungilRepository extends JpaRepository<Nungil, Long> {
    Slice<Nungil> findAllByMemberAndStatus(PageRequest request, Member member, NungilStatus status);
    Optional<Nungil> findById(Long nungilId);
}