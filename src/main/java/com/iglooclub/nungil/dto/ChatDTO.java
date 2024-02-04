package com.iglooclub.nungil.dto;

import com.iglooclub.nungil.domain.ChatMessage;
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

    public static ChatDTO of(Long chatRoomId, String nickname, ChatMessage chatMessage) {
        return new ChatDTO(chatRoomId,
                nickname,
                chatMessage.getContent(),
                chatMessage.getCreatedAt());
    }
}
