package com.iglooclub.nungil.repository;

import com.iglooclub.nungil.domain.ChatMessage;
import com.iglooclub.nungil.domain.ChatRoom;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    Slice<ChatMessage> findByChatRoom(PageRequest pageRequest, ChatRoom chatRoom);
}
