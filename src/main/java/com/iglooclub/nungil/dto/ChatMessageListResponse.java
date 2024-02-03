package com.iglooclub.nungil.dto;

import com.iglooclub.nungil.domain.ChatMessage;
import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.domain.enums.AnimalFace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageListResponse {

    private AnimalFace animalFace;

    private String sender;

    private String content;

    private LocalDateTime createdAt;

    private Boolean isSender;

    public static ChatMessageListResponse create(Member member, ChatMessage chatMessage, Boolean isSender) {
        return new ChatMessageListResponse(member.getAnimalFace(), member.getNickname(), chatMessage.getContent(),
                chatMessage.getCreatedAt(), isSender);
    }
}
