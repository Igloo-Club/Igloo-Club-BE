package com.iglooclub.nungil.util.sms;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CoolSMS implements SmsStrategy {
    private final DefaultMessageService messageService;

    private final String sender;

    public CoolSMS(@Value("${sms.coolsms.key}") String apiKey,
                   @Value("${sms.coolsms.secret}") String apiSecretKey,
                   @Value("${sms.coolsms.sender}") String sender) {
        this.sender = sender;
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");
    }

    public void send(String to, String text) {
        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom(sender);
        message.setTo(to);
        message.setText(text);

        this.messageService.sendOne(new SingleMessageSendingRequest(message));
    }
}
