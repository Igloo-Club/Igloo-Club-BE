package com.iglooclub.nungil.dto;

import com.iglooclub.nungil.domain.ChatMessage;
import com.iglooclub.nungil.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {

    private Long chatRoomId;
    private String sender;
    private String content;
    private LocalDateTime createdAt;
    private Long senderId;

    public static ChatDTO of(Long chatRoomId, Member member, ChatMessage chatMessage) {
        return new ChatDTO(chatRoomId,
                member.getNickname(),
                chatMessage.getContent(),
                chatMessage.getCreatedAt(),
                member.getId());
    }
}
