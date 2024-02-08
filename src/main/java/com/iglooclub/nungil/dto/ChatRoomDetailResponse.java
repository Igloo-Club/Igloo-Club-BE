package com.iglooclub.nungil.dto;

import com.iglooclub.nungil.domain.Company;
import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.domain.enums.AnimalFace;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomDetailResponse {

    private String animalFace;

    private String nickname;

    private String companyName;

    private String job;

    private Long ownMemberId;

    private Slice<ChatMessageListResponse> messageSlice;


    public static ChatRoomDetailResponse create(Member member, Member opponent, Slice<ChatMessageListResponse> messageSlice) {
        ChatRoomDetailResponse response = new ChatRoomDetailResponse();

        AnimalFace animalFace = opponent.getAnimalFace();
        response.animalFace = (animalFace != null) ? animalFace.getTitle() : null;

        Company company = opponent.getCompany();
        response.companyName = (company != null) ? company.getCompanyName() : null;

        response.nickname = opponent.getNickname();
        response.job = opponent.getJob();

        response.ownMemberId = member.getId();

        response.messageSlice = messageSlice;

        return response;
    }
}
