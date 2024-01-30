package com.iglooclub.nungil.dto;

import com.iglooclub.nungil.domain.enums.AnimalFace;
import com.iglooclub.nungil.domain.enums.NungilStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class NungilRequest {

    @NotNull
    private NungilStatus status;

    @NotNull
    int page;

    @NotNull
    int size;
}
