package com.iglooclub.nungil.dto;

import com.iglooclub.nungil.domain.enums.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NungilResponse {
    private Long id;

    private String location;

    private Sex sex;

    private Integer age;

    private String nickname;

    private String companyName;

    private String animalFace;

    private String alcohol;

    private String smoke;

    private String religion;

    private Mbti mbti;

    private String job;

    private Integer height;

    private String marriageState;

    private String faceDepictionAllocationList;

    private String personalityDepictionAllocationList;

    private String description;

    private String hobbyAllocationList;

    private LocalDateTime expiredAt;
}
