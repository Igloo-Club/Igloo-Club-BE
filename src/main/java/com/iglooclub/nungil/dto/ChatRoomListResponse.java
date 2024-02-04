package com.iglooclub.nungil.dto;

import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.domain.enums.AnimalFace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomListResponse {
    private AnimalFace animalFace;

    private String senderNickName;

    private String content;

    private LocalDateTime createdAt;

    private Long chatRoomId;
}
