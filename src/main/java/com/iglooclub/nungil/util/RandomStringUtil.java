package com.iglooclub.nungil.util;

import java.util.Random;

public class RandomStringUtil {
    private static final int NUMERIC_LEFT = 48;     // 아스키코드 : 0
    private static final int NUMERIC_RIGHT = 57;    // 아스키코드 : 9
    private static final int ALPHA_LEFT = 65;       // 아스키코드 : A
    private static final int ALPHA_RIGHT = 90;      // 아스키코드 : Z

    public static String alphanumeric(int length) {
        Random random = new Random();
        return random.ints(NUMERIC_LEFT, ALPHA_RIGHT + 1)
                .filter(i -> (i <= NUMERIC_RIGHT || i >= ALPHA_LEFT))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
