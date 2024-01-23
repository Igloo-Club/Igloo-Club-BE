package com.iglooclub.nungil.domain;

import com.iglooclub.nungil.domain.enums.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private OauthInfo oauthInfo;

    private String oauthAccess;

    private String nickname;

    @Enumerated(value = EnumType.STRING)
    private Sex sex;

    private LocalDateTime birthdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    private Boolean disableCompany;

    @Enumerated(value = EnumType.STRING)
    private AnimalFace animalFace;

    @Enumerated(value = EnumType.STRING)
    private Alcohol alcohol;

    @Enumerated(value = EnumType.STRING)
    private Smoke smoke;

    @Enumerated(value = EnumType.STRING)
    private Religion religion;

    @Enumerated(value = EnumType.STRING)
    private Mbti mbti;

    private String job;

    private Integer height;

    @Enumerated(value = EnumType.STRING)
    private MarriageState marriageState;

    @Enumerated(value = EnumType.STRING)
    private FaceDepiction faceDepiction;

    @Enumerated(value = EnumType.STRING)
    private PersonalityDepiction personalityDepiction;

    private String description;

    private Integer point = 0;

    @OneToMany(mappedBy = "member")
    private List<AvailableTimeAllocation> availableTimeList = new ArrayList<>();

    private Integer noshowCount = 0;

    @OneToMany(mappedBy = "member")
    private List<Hobby> hobbyList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    private Contact contact;

    @OneToMany(mappedBy = "member")
    private List<Nungil> nungilList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Acquaintance> acquaintanceList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<LocationAllocation> locationList;

    @OneToMany(mappedBy = "member")
    private List<MarkerAllocation> markerList;

    public Member update(String oauthAccess) {
        this.oauthAccess = oauthAccess;
        return this;
    }
}

