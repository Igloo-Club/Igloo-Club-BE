package com.iglooclub.nungil.repository;

import com.iglooclub.nungil.domain.ChatRoom;
import com.iglooclub.nungil.domain.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, PagingAndSortingRepository<ChatRoom, Long> {

    List<ChatRoom> findByExpiredAtBefore(LocalDateTime dateTime);
    Slice<ChatRoom> findBySenderOrReceiver(Member sender, Member receiver, Pageable pageable);

    @Query("select c from ChatRoom c where (c.sender = :member1 and c.receiver = :member2) or (c.sender = :member2 and c.receiver = :member1)")
    Optional<ChatRoom> findByMembers(@Param("member1") Member member1, @Param("member2") Member member2);
}
