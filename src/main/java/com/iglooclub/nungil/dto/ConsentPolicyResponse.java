package com.iglooclub.nungil.dto;

import com.iglooclub.nungil.domain.ConsentPolicy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConsentPolicyResponse {

    private Boolean agreeMarketing;

    public static ConsentPolicyResponse create(ConsentPolicy consentPolicy) {
        ConsentPolicyResponse response = new ConsentPolicyResponse();

        response.agreeMarketing = consentPolicy != null && consentPolicy.getAgreeMarketing();

        return response;
    }
}
