package com.iglooclub.nungil.domain;

import com.iglooclub.nungil.domain.enums.NungilStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Acquaintance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acquaintance_member_id")
    private Member acquaintanceMember;

    @Enumerated(EnumType.STRING)
    private NungilStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime sentAt;

    private LocalDateTime expiredAt;

    // == 생성 메서드 == //
    public static Acquaintance create(Member member, Member acquaintanceMember, NungilStatus status) {
        Acquaintance acquaintance = new Acquaintance();

        acquaintance.member = member;
        acquaintance.acquaintanceMember = acquaintanceMember;
        acquaintance.status = status;
        acquaintance.createdAt = LocalDateTime.now();
        if(NungilStatus.RECEIVED.equals(status)){
            acquaintance.sentAt = LocalDateTime.now();
            acquaintance.expiredAt = acquaintance.sentAt.plusDays(7);
        }
        return acquaintance;
    }

    // == 비즈니스 로직 == //

    public void update(NungilStatus status) {
        this.status = status;
        this.sentAt = LocalDateTime.now();
        this.expiredAt = this.sentAt.plusDays(7);
    }
}
