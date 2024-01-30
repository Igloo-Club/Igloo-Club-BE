package com.iglooclub.nungil.util.sms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SmsSender {

    private SmsStrategy strategy;

    public void send(String to, String text) {
        this.strategy.send(to, text);
    }
}
