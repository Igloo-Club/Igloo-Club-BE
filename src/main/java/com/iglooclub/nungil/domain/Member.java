package com.iglooclub.nungil.domain;

import com.iglooclub.nungil.domain.enums.*;
import com.iglooclub.nungil.dto.ProfileCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
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

    private LocalDate birthdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @Builder.Default
    private Boolean disableCompany = true;

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

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FaceDepictionAllocation> faceDepictionAllocationList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PersonalityDepictionAllocation> personalityDepictionAllocationList = new ArrayList<>();

    @Column(length = 400)
    private String description;

    @Builder.Default
    private Integer point = 0;

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvailableTimeAllocation> availableTimeAllocationList = new ArrayList<>();

    @Builder.Default
    private Integer noshowCount = 0;

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HobbyAllocation> hobbyAllocationList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Contact contact;

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Nungil> nungilList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Acquaintance> acquaintanceList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<LocationAllocation> locationList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MarkerAllocation> markerAllocationList = new ArrayList<>();

    public Member update(String oauthAccess) {
        this.oauthAccess = oauthAccess;
        return this;
    }

    public Member(){
        this.point = 0;
        this.disableCompany = true;
        this.availableTimeAllocationList = new ArrayList<>();
        this.noshowCount = 0;
        this.hobbyAllocationList = new ArrayList<>();
        this.nungilList = new ArrayList<>();
        this.acquaintanceList = new ArrayList<>();
        this.faceDepictionAllocationList = new ArrayList<>();
        this.personalityDepictionAllocationList = new ArrayList<>();
        this.markerAllocationList = new ArrayList<>();
        this.locationList = new ArrayList<>();
    }

    public void createProfile(ProfileCreateRequest request) {
        Contact newContact = Contact.builder()
                .kakao(request.getContactKakao())
                .instagram(request.getContactInstagram())
                .build();

        this.nickname = request.getNickname();
        this.sex = request.getSex();
        this.birthdate = request.getBirthdate();
        this.contact = newContact;
        this.animalFace = request.getAnimalFace();
        this.job = request.getJob();
        this.height = request.getHeight();
        this.mbti = request.getMbti();
        this.marriageState = request.getMarriageState();
        this.religion = request.getReligion();
        this.alcohol = request.getAlcohol();
        this.smoke = request.getSmoke();
        request.getFaceDepictionList().forEach(this::addFaceDepiction);
        request.getPersonalityDepictionList().forEach(this::addPersonalityDepiction);
        this.description = request.getDescription();
        request.getMarkerList().forEach(this::addMarker);
        request.getAvailableTimeList().forEach(this::addAvailableTime);
        request.getHobbyList().forEach(this::addHobby);

    }

    public void addFaceDepiction(FaceDepiction faceDepiction) {
        FaceDepictionAllocation faceDepictionAllocation = FaceDepictionAllocation.builder()
                .faceDepiction(faceDepiction)
                .member(this)
                .build();
        this.faceDepictionAllocationList.add(faceDepictionAllocation);
    }

    public void addPersonalityDepiction(PersonalityDepiction personalityDepiction) {
        PersonalityDepictionAllocation personalityDepictionAllocation = PersonalityDepictionAllocation.builder()
                .personalityDepiction(personalityDepiction)
                .member(this)
                .build();
        this.personalityDepictionAllocationList.add(personalityDepictionAllocation);
    }

    public void addMarker(Marker marker) {
        MarkerAllocation markerAllocation = MarkerAllocation.builder()
                .marker(marker)
                .member(this)
                .build();
        this.markerAllocationList.add(markerAllocation);
    }

    public void addAvailableTime(AvailableTime availableTime) {
        AvailableTimeAllocation availableTimeAllocation = AvailableTimeAllocation.builder()
                .availableTime(availableTime)
                .member(this)
                .build();
        this.availableTimeAllocationList.add(availableTimeAllocation);
    }

    public void addHobby(Hobby hobby) {
        HobbyAllocation hobbyAllocation = HobbyAllocation.builder()
                .hobby(hobby)
                .member(this)
                .build();
        this.hobbyAllocationList.add(hobbyAllocation);
    }
}
