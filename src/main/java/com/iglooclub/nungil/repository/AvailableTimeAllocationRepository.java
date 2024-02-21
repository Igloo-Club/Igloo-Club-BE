package com.iglooclub.nungil.repository;

import com.iglooclub.nungil.domain.AvailableTimeAllocation;
import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.domain.enums.AvailableTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AvailableTimeAllocationRepository extends JpaRepository<AvailableTimeAllocation, Long> {
    @Modifying
    @Query("delete from AvailableTimeAllocation f where f.availableTime not in :availableTimeList")
    void deleteAllByAvailableTimeNotIn(@Param("availableTimeList") List<AvailableTime> availableTimeList);

    @Modifying
    @Query("delete from AvailableTimeAllocation f where f.member = :member")
    void deleteAllByMember(@Param("member") Member member);
}
