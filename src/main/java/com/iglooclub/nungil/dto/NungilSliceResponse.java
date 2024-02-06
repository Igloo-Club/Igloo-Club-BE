package com.iglooclub.nungil.dto;

import com.iglooclub.nungil.domain.Company;
import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.domain.Nungil;
import com.iglooclub.nungil.domain.enums.AnimalFace;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NungilSliceResponse {
    private Long nungilId;

    private String animalFace;

    private String companyName;

    private String job;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime expiredAt;

    private String nickname;

    public static NungilSliceResponse create(Nungil nungil, Member member) {
        NungilSliceResponse response = new NungilSliceResponse();
        AnimalFace animalFace = member.getAnimalFace();
        Company company = member.getCompany();

        response.nungilId = nungil.getId();
        response.createdAt = nungil.getCreatedAt();
        response.expiredAt = nungil.getExpiredAt();

        response.animalFace = (animalFace != null) ? animalFace.getTitle() : null;
        response.companyName = (company != null) ? company.getCompanyName() : null;
        response.job = member.getJob();
        response.description = member.getDescription();
        response.nickname = member.getNickname();

        return response;
    }
}
