package com.iglooclub.nungil.repository;

import com.iglooclub.nungil.domain.PersonalityDepictionAllocation;
import com.iglooclub.nungil.domain.enums.PersonalityDepiction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonalityDepictionAllocationRepository extends JpaRepository<PersonalityDepictionAllocation, Long> {
    @Modifying
    @Query("delete from PersonalityDepictionAllocation f where f.personalityDepiction not in :personalityDepictionList")
    void deleteAllByPersonalityDepictionNotIn(@Param("personalityDepictionList") List<PersonalityDepiction> personalityDepictionList);
}
