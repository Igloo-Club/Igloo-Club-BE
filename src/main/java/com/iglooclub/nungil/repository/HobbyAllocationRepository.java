package com.iglooclub.nungil.repository;

import com.iglooclub.nungil.domain.HobbyAllocation;
import com.iglooclub.nungil.domain.enums.Hobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HobbyAllocationRepository extends JpaRepository<HobbyAllocation, Long> {
    @Modifying
    @Query("delete from HobbyAllocation f where f.hobby not in :hobbyList")
    void deleteAllByHobbyNotIn(@Param("hobbyList") List<Hobby> hobbyList);
}
