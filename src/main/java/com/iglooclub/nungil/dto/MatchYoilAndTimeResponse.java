package com.iglooclub.nungil.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iglooclub.nungil.domain.enums.Yoil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class MatchYoilAndTimeResponse {
    private Yoil matchYoil;

    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate matchDate;
}
