package com.iglooclub.nungil.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nullable
    private String kakao;

    @Nullable
    private String instagram;

    // == 비즈니스 로직 == //

    public void update(String kakao, String instagram) {
        this.kakao = kakao;
        this.instagram = instagram;
    }
}
