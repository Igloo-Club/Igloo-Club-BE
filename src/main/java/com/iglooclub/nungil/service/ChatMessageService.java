package com.iglooclub.nungil.service;

import com.iglooclub.nungil.domain.ChatMessage;
import com.iglooclub.nungil.domain.ChatRoom;
import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.dto.ChatDTO;
import com.iglooclub.nungil.dto.ChatMessageListResponse;
import com.iglooclub.nungil.exception.ChatRoomErrorResult;
import com.iglooclub.nungil.exception.GeneralException;
import com.iglooclub.nungil.repository.ChatMessageRepository;
import com.iglooclub.nungil.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

        ChatRoom chatRoom = getChatRoom(chatDTO.getChatRoomId());

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
     * 주어진 채팅방의 메시지 목록을 Slice 형식으로 조회하는 메서드입니다.
     * @param chatRoomId 채팅방 ID
     * @param member 조회를 요청한 회원의 엔티티
     * @param pageRequest 조회되는 페이지 번호, 갯수, 정렬 방식(최근순)
     * @return Slice 형식의 채팅 메시지 목록
     */
    public Slice<ChatMessageListResponse> getMessageSlice(Long chatRoomId, Member member, PageRequest pageRequest) {
        ChatRoom chatRoom = getChatRoom(chatRoomId);

        // 메시지 발행자(member)가 해당 채팅방의 일원이 아니면 예외 발생
        if (!checkChatRoomMember(chatRoom, member)) {
            throw new GeneralException(ChatRoomErrorResult.NOT_MEMBER);
        }

        // 주어진 채팅방의 메시지들을 pageRequest에 맞추어 조회
        Slice<ChatMessage> messageSlice = chatMessageRepository.findByChatRoom(pageRequest, chatRoom);

        // 메시지들을 DTO 리스트로 변환
        List<ChatMessageListResponse> responseList = messageSlice.getContent().stream()
                .map(chatMessage -> {
                    Member sender = chatMessage.getMember();
                    Boolean isSender = member.getId().equals(sender.getId());

                    return ChatMessageListResponse.create(sender, chatMessage, isSender);
                }).collect(Collectors.toList());

        // 변환된 DTO 리스트와 함께 새로운 Slice 객체를 생성하여 반환
        return new SliceImpl<>(responseList, pageRequest, messageSlice.hasNext());
    }

    private ChatRoom getChatRoom(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new GeneralException(ChatRoomErrorResult.CHAT_ROOM_NOT_FOUND));
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




    /**
     * 매 시 정각 expireAt이 초과된 채팅룸/채팅 내역을 삭제합니다
     *
     *
     */
    @Scheduled(cron = "0 0 * * * *") // 매시 정각에 실행
    @Transactional
    public void deleteExpiredChatRoom() {
        LocalDateTime now = LocalDateTime.now();
        List<ChatRoom> chatRoomList = chatRoomRepository.findByExpiredAtBefore(now);
        for (ChatRoom chatRoom : chatRoomList) {
            chatRoomRepository.delete(chatRoom);
        }
    }

}