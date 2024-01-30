package com.iglooclub.nungil.dto;

import com.iglooclub.nungil.domain.Contact;
import com.iglooclub.nungil.domain.FaceDepictionAllocation;
import com.iglooclub.nungil.domain.enums.Hobby;
import com.iglooclub.nungil.domain.PersonalityDepictionAllocation;
import com.iglooclub.nungil.domain.enums.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class NungilResponse {
    private Long id;

    private Location location;

    private Sex sex;

    private Integer age;

    private String nickname;

    private String companyName;

    private AnimalFace animalFace;

    private Alcohol alcohol;

    private Smoke smoke;

    private Religion religion;

    private Mbti mbti;

    private String job;

    private Integer height;

    private MarriageState marriageState;

    private String faceDepictionAllocationList;

    private String personalityDepictionAllocationList;

    private String description;

    private String hobbyAllocationList;

}
