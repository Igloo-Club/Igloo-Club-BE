package com.iglooclub.nungil.dto;

import com.iglooclub.nungil.domain.enums.NungilStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NungilDetailRequest {
    @NotNull
    private Long nungilId;
}
