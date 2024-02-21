package com.iglooclub.nungil.util;

public class TokenUtil {

    public final static String HEADER_AUTHORIZATION = "Authorization";

    public final static String TOKEN_PREFIX = "Bearer ";

    public static String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
