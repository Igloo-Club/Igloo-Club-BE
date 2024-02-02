package com.iglooclub.nungil.domain;

import com.iglooclub.nungil.domain.enums.ChatMessageStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 400)
    private String content;

    // 개발의 편의를 위해 일단 기본값을 READ로 설정. 추후 읽지 않은 메시지 개발 시 UNREAD로 변경
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ChatMessageStatus status = ChatMessageStatus.READ;

    private LocalDateTime createdAt;

    public static ChatMessage create(ChatRoom chatRoom, Member member, String content) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.chatRoom = chatRoom;
        chatMessage.member = member;
        chatMessage.content = content;
        chatMessage.createdAt = LocalDateTime.now();

        return chatMessage;
    }
}
