package com.iglooclub.nungil.domain.events;

import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.domain.Nungil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Getter
@Validated
@RequiredArgsConstructor
public class NungilMatchedEvent {

    @NotNull
    private final Member sender;

    @NotNull
    private final Nungil sentNungil;

    public String getMatcherPhoneNumber(){
        return sender.getPhoneNumber();
    }
}
