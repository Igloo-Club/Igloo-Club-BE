package com.iglooclub.nungil.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageAuthenticationRequest {
    private String phoneNumber;
}
