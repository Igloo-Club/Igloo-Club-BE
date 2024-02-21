package com.iglooclub.nungil.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyListResponse {

    private String email;

    private List<String> companyNameList;

    private Integer totalCount;

    // == 생성 메서드 == //
    public static CompanyListResponse create(List<String> companyNameList, String email) {
        return new CompanyListResponse(email, companyNameList, companyNameList.size());
    }
}
