package com.iglooclub.nungil.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Location {
    PANGYO("판교"),
    GWANGHWAMUN("광화문"),
    ;

    private final String name;
}
