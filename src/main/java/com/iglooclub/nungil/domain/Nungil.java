package com.iglooclub.nungil.domain;

import com.iglooclub.nungil.domain.enums.AvailableTime;
import com.iglooclub.nungil.domain.enums.Marker;
import com.iglooclub.nungil.domain.enums.NungilStatus;
import com.iglooclub.nungil.domain.enums.Yoil;
import com.iglooclub.nungil.util.converter.MarkerListConverter;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Nungil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private NungilStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime expiredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    // 리스트를 쉼표로 구분된 문자열로 DB에 저장
    @Convert(converter = MarkerListConverter.class)
    @Builder.Default
    private List<Marker> matchedMarkers = new ArrayList<>();

    @Nullable
    @Enumerated(EnumType.STRING)
    private AvailableTime matchedAvailableTime;

    // == 정적 생성 메서드 == //
    public static Nungil create(Member member, Member receiver, NungilStatus status) {
        return Nungil.builder()
                .member(member)
                .receiver(receiver)
                .createdAt(LocalDateTime.now())
                .status(status)
                .build();
    }

    public void setStatus(NungilStatus status){
        this.status = status;
    }

    public void setExpiredAt7DaysAfter(){
        this.expiredAt = LocalDateTime.now().plusDays(7);
    }

    public void setExpiredAtNull(){
        this.expiredAt = null;
    }

    public void update(List<Marker> matchedMarkers, AvailableTime matchedAvailableTime) {
        this.matchedMarkers.clear();
        this.matchedMarkers.addAll(matchedMarkers);
        this.matchedAvailableTime = matchedAvailableTime;
    }


}
