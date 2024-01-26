package com.iglooclub.nungil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRecommendRequest {
    @NotNull
    private Boolean isPayed;
}
