package com.iglooclub.nungil.domain;

import com.iglooclub.nungil.domain.enums.*;
import com.iglooclub.nungil.dto.ProfileUpdateRequest;
import com.iglooclub.nungil.util.converter.YoilListConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private String phoneNumber;

    @Enumerated(value = EnumType.STRING)
    private Sex sex;

    private LocalDate birthdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    private String email;

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

    @Column(length = 1000)
    private String description;

    @Builder.Default
    private Integer point = 0;

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvailableTimeAllocation> availableTimeAllocationList = new ArrayList<>();

    @Builder.Default
    private Integer noshowCount = 0;

    @Enumerated(value = EnumType.STRING)
    private Location location;

    private Integer preferredAgeStart;

    private Integer preferredAgeEnd;

    // 인연 프로필 뽑기 횟수
    @Builder.Default
    private Long drawCount = 0L;

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HobbyAllocation> hobbyAllocationList = new ArrayList<>();

    @Builder.Default
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Contact contact = Contact.builder().build();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Nungil> nungilList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Acquaintance> acquaintanceList = new ArrayList<>();

    @Convert(converter = YoilListConverter.class)
    @Builder.Default
    private List<Yoil> yoilList = new ArrayList<>();

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
        this.yoilList = new ArrayList<>();
        this.drawCount = 0L;
    }

    // == 비즈니스 로직 == //
    /**
     * 회원의 프로필 정보를 수정하는 메서드이다.
     * @param request 프로필 수정 요청 DTO
     */
    public void updateProfile(ProfileUpdateRequest request) {

        this.nickname = request.getNickname();
        this.sex = request.getSex();
        this.birthdate = request.getBirthdate();
        this.contact.update(request.getContactKakao(), request.getContactInstagram());
        this.animalFace = request.getAnimalFace();
        this.job = request.getJob();
        this.height = request.getHeight();
        this.mbti = request.getMbti();
        this.marriageState = request.getMarriageState();
        this.religion = request.getReligion();
        this.alcohol = request.getAlcohol();
        this.smoke = request.getSmoke();
        this.description = request.getDescription();

        this.faceDepictionAllocationList.clear();
        addAllFaceDepiction(request.getFaceDepictionList());

        this.personalityDepictionAllocationList.clear();
        addAllPersonalityDepiction(request.getPersonalityDepictionList());

        this.hobbyAllocationList.clear();
        addAllHobby(request.getHobbyList());
    }

    // == 조회 로직 == //

    public List<FaceDepiction> getFaceDepictionList() {
        return this.faceDepictionAllocationList.stream()
                .map(FaceDepictionAllocation::getFaceDepiction)
                .collect(Collectors.toList());
    }

    public List<PersonalityDepiction> getPersonalityDepictionList() {
        return this.personalityDepictionAllocationList.stream()
                .map(PersonalityDepictionAllocation::getPersonalityDepiction)
                .collect(Collectors.toList());
    }

    public List<Marker> getMarkerList() {
        return this.markerAllocationList.stream()
                .map(MarkerAllocation::getMarker)
                .collect(Collectors.toList());
    }

    public List<AvailableTime> getAvailableTimeList() {
        return this.availableTimeAllocationList.stream()
                .map(AvailableTimeAllocation::getAvailableTime)
                .collect(Collectors.toList());
    }

    public List<Hobby> getHobbyList() {
        return this.hobbyAllocationList.stream()
                .map(HobbyAllocation::getHobby)
                .collect(Collectors.toList());
    }

    // == 연관관계 메서드 == //

    public void addAllFaceDepiction(List<FaceDepiction> faceDepictionList) {
        List<FaceDepictionAllocation> faceDepictionAllocationList = faceDepictionList.stream()
                .map(v -> FaceDepictionAllocation.builder()
                        .faceDepiction(v)
                        .member(this)
                        .build())
                .collect(Collectors.toList());
        this.faceDepictionAllocationList.addAll(faceDepictionAllocationList);
    }

    public void addAllPersonalityDepiction(List<PersonalityDepiction> personalityDepictionList) {
        List<PersonalityDepictionAllocation> personalityDepictionAllocationList = personalityDepictionList.stream()
                .map(v -> PersonalityDepictionAllocation.builder()
                        .personalityDepiction(v)
                        .member(this)
                        .build())
                .collect(Collectors.toList());
        this.personalityDepictionAllocationList.addAll(personalityDepictionAllocationList);
    }

    public void addAllHobby(List<Hobby> hobbyList) {
        List<HobbyAllocation> hobbyAllocationList = hobbyList.stream()
                .map(v -> HobbyAllocation.builder()
                        .hobby(v)
                        .member(this)
                        .build())
                .collect(Collectors.toList());
        this.hobbyAllocationList.addAll(hobbyAllocationList);
    }

    public void setCompany(String email, Company company) {
        this.email = email;
        this.company = company;
    }

    public void updateSchedule(Location location, List<Yoil> yoilList, List<AvailableTime> availableTimeList, List<Marker> markerList) {
        List<AvailableTimeAllocation> newAvailableTimeAllocationList = availableTimeList.stream()
                .map(v -> AvailableTimeAllocation.builder().availableTime(v).member(this).build())
                .collect(Collectors.toList());

        this.location = location;
        this.yoilList = yoilList;

        this.availableTimeAllocationList.clear();
        this.availableTimeAllocationList.addAll(newAvailableTimeAllocationList);


        List<MarkerAllocation> newMarkerAllocationList = markerList.stream()
                .map(v -> MarkerAllocation.builder().marker(v).member(this).build())
                .collect(Collectors.toList());

        this.markerAllocationList.clear();
        this.markerAllocationList.addAll(newMarkerAllocationList);
    }


    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void updateLocation(Location location) {this.location = location;}

    public void plusDrawCount() {this.drawCount += 1L;}


    // List를 String으로 변환하는 메서드
    public String getFaceDepictionAllocationsAsString() {
        return faceDepictionAllocationList.stream()
                .map(FaceDepictionAllocation::getFaceDepiction)
                .map(FaceDepiction::getTitle)
                .collect(Collectors.joining(", "));
    }
    public String getPersonalityDepictionAllocationAsString() {
        return personalityDepictionAllocationList.stream()
                .map(PersonalityDepictionAllocation::getPersonalityDepiction)
                .map(PersonalityDepiction::getTitle)
                .collect(Collectors.joining(", "));
    }
    public String getHobbyAllocationAsString() {
        return hobbyAllocationList.stream()
                .map(HobbyAllocation::getHobby)
                .map(Hobby::getTitle)
                .collect(Collectors.joining(", "));
    }


    public Boolean updateDisableCompany(Boolean disableCompany) {
        this.disableCompany = disableCompany;
        return this.disableCompany;
    }
}
