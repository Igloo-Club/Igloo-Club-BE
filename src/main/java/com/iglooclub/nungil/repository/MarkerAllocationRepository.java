package com.iglooclub.nungil.repository;

import com.iglooclub.nungil.domain.MarkerAllocation;
import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.domain.enums.Marker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MarkerAllocationRepository extends JpaRepository<MarkerAllocation, Long> {
    @Modifying
    @Query("delete from MarkerAllocation f where f.marker not in :markerList")
    void deleteAllByMarkerNotIn(@Param("markerList") List<Marker> markerList);

    @Modifying
    @Query("delete from MarkerAllocation f where f.member = :member")
    void deleteAllByMember(@Param("member") Member member);
}
