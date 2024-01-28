package com.iglooclub.nungil.repository;

import com.iglooclub.nungil.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    List<Company> findTop8ByEmailOrderByCompanyNameAsc(String email);

    Optional<Company> findByCompanyNameAndEmail(String companyName, String email);
}
