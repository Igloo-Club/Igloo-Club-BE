package com.iglooclub.nungil.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class NungilSliceResponse {
    private final Long nungilId;

    private final String animalFace;

    private final String companyName;

    private final String job;

    private final String description;

    private final LocalDateTime createdAt;

    private final LocalDateTime expiredAt;
}
