package com.iglooclub.nungil.repository;

import com.iglooclub.nungil.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    List<ChatRoom> findByExpiredAtBefore(LocalDateTime dateTime);
}
