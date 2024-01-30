package com.iglooclub.nungil.util.sms;

public interface SmsStrategy {

    void send(String to, String text);
}
