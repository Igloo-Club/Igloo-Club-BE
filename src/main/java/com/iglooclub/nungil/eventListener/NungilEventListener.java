package com.iglooclub.nungil.eventListener;

import com.iglooclub.nungil.domain.events.NungilMatchedEvent;
import com.iglooclub.nungil.domain.events.NungilSentEvent;
import com.iglooclub.nungil.util.CoolSMS;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NungilEventListener {
    private final CoolSMS coolSMS;

    private static final String BASE_URL = "https://nungil.com";

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void matchNungilListen(NungilMatchedEvent nungilMatchedEvent){
        // 눈길 보낸 사용자에게 알림 전송
        String phoneNumber = nungilMatchedEvent.getSender().getPhoneNumber();
        String url = BASE_URL + "/finishmatch/" + nungilMatchedEvent.getSentNungil().getId();
        String text = "[눈길] 축하해요! 서로의 눈길이 닿았어요. 채팅방을 통해 두 분의 첫만남 약속을 잡아보세요.\n" + url;

        coolSMS.send(phoneNumber, text);
    }

    @Async
    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sentNungilListen(NungilSentEvent nungilSentEvent){
        // 눈길 보낸 사용자에게 알림 전송
        String phoneNumber = nungilSentEvent.getSender().getPhoneNumber();
        String url = BASE_URL + "/receiveddetailpage/" + nungilSentEvent.getSentNungil().getId();
        String text = "[눈길] 새로운 눈길이 도착했어요. 얼른 확인해보세요!\n" + url;

        coolSMS.send(phoneNumber, text);
    }
}
