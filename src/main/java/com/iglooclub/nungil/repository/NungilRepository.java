package com.iglooclub.nungil.repository;

import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.domain.Nungil;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NungilRepository extends JpaRepository<Nungil, Long> {
}
