package com.iglooclub.nungil.repository;

import com.iglooclub.nungil.domain.ChatRoom;
import com.iglooclub.nungil.domain.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, PagingAndSortingRepository<ChatRoom, Long> {

    List<ChatRoom> findByExpiredAtBefore(LocalDateTime dateTime);
    Slice<ChatRoom> findBySenderOrReceiver(Member sender, Member receiver, Pageable pageable);
}
