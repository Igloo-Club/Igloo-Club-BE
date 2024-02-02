package com.iglooclub.nungil.controller;

import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.dto.ChatDTO;
import com.iglooclub.nungil.service.ChatMessageService;
import com.iglooclub.nungil.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessagingTemplate messagingTemplate;

    private final ChatMessageService chatMessageService;

    private final MemberService memberService;

    @MessageMapping("/send")
    public void send(@Payload ChatDTO chatDTO, Principal principal) {
        Member member = getMember(principal);

        ChatDTO mappedChat = chatMessageService.save(chatDTO, member);

        messagingTemplate.convertAndSend("/topic/" + chatDTO.getChatRoomId(), mappedChat);
    }

    private Member getMember(Principal principal) {
        return memberService.findById(Long.parseLong(principal.getName()));
    }
}
