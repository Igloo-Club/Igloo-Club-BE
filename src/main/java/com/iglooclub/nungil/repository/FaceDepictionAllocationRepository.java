package com.iglooclub.nungil.repository;

import com.iglooclub.nungil.domain.FaceDepictionAllocation;
import com.iglooclub.nungil.domain.enums.FaceDepiction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FaceDepictionAllocationRepository extends JpaRepository<FaceDepictionAllocation, Long> {
    @Modifying
    @Query("delete from FaceDepictionAllocation f where f.faceDepiction not in :faceDepictionList")
    void deleteAllByFaceDepictionNotIn(@Param("faceDepictionList") List<FaceDepiction> faceDepictionList);
}
