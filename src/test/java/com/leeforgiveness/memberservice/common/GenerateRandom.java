package com.leeforgiveness.memberservice.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

public class GenerateRandom {
    private static final String CHARACTERS = "0123456789";
    private static final Random RANDOM = new Random();

    private static String string(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    public static String subscriberUuid() {
        return UUID.randomUUID().toString();
    }

    public static String influencerUuid() {
        return string(9);
    }
}
