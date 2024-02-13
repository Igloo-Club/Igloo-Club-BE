package com.iglooclub.nungil.controller;

import com.iglooclub.nungil.config.jwt.TokenProvider;
import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.dto.*;
import com.iglooclub.nungil.service.ChatMessageService;
import com.iglooclub.nungil.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static com.iglooclub.nungil.util.TokenUtil.HEADER_AUTHORIZATION;
import static com.iglooclub.nungil.util.TokenUtil.getAccessToken;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessagingTemplate messagingTemplate;

    private final ChatMessageService chatMessageService;

    private final MemberService memberService;

    private final TokenProvider tokenProvider;

    @MessageMapping("/send")
    public void send(@Payload ChatDTO chatDTO, @Header(HEADER_AUTHORIZATION) String authorizationHeader) {
        String accessToken = getAccessToken(authorizationHeader);
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        Member member = getMember(authentication);

        ChatDTO mappedChat = chatMessageService.save(chatDTO, member);

        messagingTemplate.convertAndSend("/topic/" + chatDTO.getChatRoomId(), mappedChat);
    }

    @GetMapping("/api/chat/room/{chatRoomId}")
    public ResponseEntity<ChatRoomDetailResponse> getMessageSlice(@PathVariable Long chatRoomId,
                                                                          @RequestParam(defaultValue = "0") int pageNumber,
                                                                          @RequestParam(defaultValue = "12") int pageSize,
                                                                          Principal principal) {

        Member member = getMember(principal);
        // 메시지를 최근에 작성된 순서대로 조회
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc("createdAt")));

        ChatRoomDetailResponse chatRoomDetail = chatMessageService.getChatRoomDetail(chatRoomId, member, pageRequest);

        return new ResponseEntity<>(chatRoomDetail, HttpStatus.OK);
    }

    @GetMapping("api/chat/room")
    public ResponseEntity<Slice<ChatRoomListResponse>> getRoomSlice(@RequestParam(defaultValue = "0") int pageNumber,
                                                                    @RequestParam(defaultValue = "12") int pageSize,
                                                                    Principal principal) {

        Member member = getMember(principal);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        Slice<ChatRoomListResponse> roomSlice = chatMessageService.getChatRoomSlice(member, pageRequest);

        return new ResponseEntity<>(roomSlice, HttpStatus.OK);
    }

    @GetMapping("/api/chat/room/{chatRoomId}/info")
    public AvailableTimeAndPlaceResponse getAvailableTimeAndPlace(@PathVariable Long chatRoomId, Principal principal){
        Member member = getMember(principal);
        return chatMessageService.getAvailableTimeAndPlace(member, chatRoomId);
    }

    private Member getMember(Principal principal) {
        return memberService.findById(Long.parseLong(principal.getName()));
    }
}
