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

    public static Yoil findCommonYoil(List<Yoil> list1, List<Yoil> list2) {
        Set<DayOfWeek> set1 = EnumSet.noneOf(DayOfWeek.class);
        for (Yoil yoil : list1) {
            set1.add(yoil.getDayOfWeek());
        }

        LocalDateTime now = LocalDateTime.now();
        boolean isAfter11AM = now.getHour() >= 11;

        for (Yoil yoil : list2) {
            if (set1.contains(yoil.getDayOfWeek())) {
                if (isAfter11AM) {
                    return findNextYoil(yoil, now);
                } else {
                    return yoil;
                }
            }
        }

        return null;
    }

    private static Yoil findNextYoil(Yoil yoil, LocalDateTime now) {
        DayOfWeek today = now.getDayOfWeek();
        int daysUntilNext = (7 + yoil.getDayOfWeek().getValue() - today.getValue()) % 7;
        if (daysUntilNext == 0) {
            daysUntilNext = 7;
        }
        return Yoil.values()[(today.getValue() + daysUntilNext - 1) % 7];
    }

}
