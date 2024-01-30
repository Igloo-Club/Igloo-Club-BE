package com.iglooclub.nungil.dto;

import com.iglooclub.nungil.domain.enums.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUpdateRequest {

    @NotBlank
    @Size(max = 8)
    private String nickname;

    @NotNull
    private Sex sex;

    @NotNull
    @Past
    private LocalDate birthdate;

    @Size(max = 255)
    private String contactKakao;

    @Size(max = 255)
    private String contactInstagram;

    @NotNull
    private AnimalFace animalFace;

    @NotNull
    @Size(max = 255)
    private String job;

    @NotNull
    private Integer height;

    @NotNull
    private Mbti mbti;

    @NotNull
    private MarriageState marriageState;

    @NotNull
    private Religion religion;

    @NotNull
    private Alcohol alcohol;

    @NotNull
    private Smoke smoke;

    @NotNull
    private List<FaceDepiction> faceDepictionList;

    @NotNull
    private List<PersonalityDepiction> personalityDepictionList;

    @NotNull
    @Size(max = 300)
    private String description;

    private List<Marker> markerList;

    private List<Hobby> hobbyList;
}