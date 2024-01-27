package com.iglooclub.nungil.repository;

import com.iglooclub.nungil.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    List<Company> findTop8ByEmailOrderByCompanyNameAsc(String email);
}
