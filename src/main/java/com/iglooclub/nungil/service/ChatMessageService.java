package com.iglooclub.nungil.service;

import com.iglooclub.nungil.domain.ChatMessage;
import com.iglooclub.nungil.domain.ChatRoom;
import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.dto.ChatDTO;
import com.iglooclub.nungil.exception.ChatRoomErrorResult;
import com.iglooclub.nungil.exception.GeneralException;
import com.iglooclub.nungil.repository.ChatMessageRepository;
import com.iglooclub.nungil.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;

    private final ChatMessageRepository chatMessageRepository;

    /**
     * 채팅 메시지를 데이터베이스에 저장하는 메서드입니다.
     * @param chatDTO 발행된 채팅 메시지 DTO
     * @param member 메시지 발행자 정보
     * @return 기존의 발행된 메시지에서 데이터가 추가된 DTO
     */
    @Transactional
    public ChatDTO save(ChatDTO chatDTO, Member member) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatDTO.getChatRoomId())
                .orElseThrow(() -> new GeneralException(ChatRoomErrorResult.CHAT_ROOM_NOT_FOUND));

        // 메시지 발행자(member)가 해당 채팅방의 일원이 아니면 예외 발생
        if (!checkChatRoomMember(chatRoom, member)) {
            throw new GeneralException(ChatRoomErrorResult.NOT_MEMBER);
        }

        // 데이터베이스에 채팅 메시지 저장
        ChatMessage chatMessage = ChatMessage.create(chatRoom, member, chatDTO.getContent());
        chatMessageRepository.save(chatMessage);

        return ChatDTO.of(chatDTO.getChatRoomId(), member.getNickname(), chatMessage);
    }

    /**
     * 사용자가 주어진 채팅방의 일원인지 확인한다.
     * @param chatRoom 채팅방
     * @param member 확인할 사용자
     * @return 채팅방에 속해있는지 여부
     */
    private boolean checkChatRoomMember(ChatRoom chatRoom, Member member) {
        Long memberId = member.getId();
        return memberId.equals(chatRoom.getReceiver().getId()) || memberId.equals(chatRoom.getSender().getId());
    }
}
