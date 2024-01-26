package com.iglooclub.nungil.domain;

import com.iglooclub.nungil.domain.enums.NungilStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Nungil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private NungilStatus status;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    // 리스트를 쉼표로 구분된 문자열로 DB에 저장
    @Nullable
    private String matchedMarkers;

    // 리스트를 쉼표로 구분된 문자열로 DB에 저장
    @Nullable
    private String matchedAvailableTimes;
}
