package com.iglooclub.nungil.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum Yoil {
    SUN("일요일", DayOfWeek.SUNDAY),
    MON("월요일", DayOfWeek.MONDAY),
    TUE("화요일", DayOfWeek.TUESDAY),
    WED("수요일", DayOfWeek.WEDNESDAY),
    THU("목요일", DayOfWeek.THURSDAY),
    FRI("금요일", DayOfWeek.FRIDAY),
    SAT("토요일", DayOfWeek.SATURDAY);

    private final String title;
    private final DayOfWeek dayOfWeek;
}
