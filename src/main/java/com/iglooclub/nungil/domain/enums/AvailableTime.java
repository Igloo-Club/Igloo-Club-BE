package com.iglooclub.nungil.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AvailableTime {
    T1100("11:00"),
    T1130("11:30"),
    T1200("12:00"),
    T1230("12:30"),
    T1300("13:00"),
    T1330("13:30"),
    T1400("14:00"),
    T1430("14:30"),
    ;

    private final String title;
}
