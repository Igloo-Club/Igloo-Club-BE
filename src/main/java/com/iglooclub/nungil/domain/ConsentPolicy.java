package com.iglooclub.nungil.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConsentPolicy {

    // 마케팅 정보 수신 동의
    private Boolean agreeMarketing = true;

    public static ConsentPolicy create(Boolean agreeMarketing) {
        ConsentPolicy consentPolicy = new ConsentPolicy();
        consentPolicy.agreeMarketing = agreeMarketing;
        return consentPolicy;
    }
}
