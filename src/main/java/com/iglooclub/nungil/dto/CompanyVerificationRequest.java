package com.iglooclub.nungil.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyVerificationRequest {

    private String code;

    private String email;

    private String companyName;
}
