package com.iglooclub.nungil.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DisableCompanyResponse {

    private Boolean disableCompany;

    public static DisableCompanyResponse create(Boolean disableCompany) {
        DisableCompanyResponse response = new DisableCompanyResponse();
        response.disableCompany = disableCompany;
        return response;
    }
}
