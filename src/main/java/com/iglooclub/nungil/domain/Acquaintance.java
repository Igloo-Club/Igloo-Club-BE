package com.iglooclub.nungil.domain;

import com.iglooclub.nungil.domain.enums.NungilStatus;
import lombok.*;

import javax.persistence.*;

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

    // == 생성 메서드 == //
    public static Acquaintance create(Member member, Member acquaintanceMember, NungilStatus status) {
        Acquaintance acquaintance = new Acquaintance();

        acquaintance.member = member;
        acquaintance.acquaintanceMember = acquaintanceMember;
        acquaintance.status = status;

        return acquaintance;
    }

    // == 비즈니스 로직 == //

    public void update(NungilStatus status) {
        this.status = status;
    }
}
