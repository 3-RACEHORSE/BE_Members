package com.leeforgiveness.memberservice.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

public class GenerateRandom {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new Random();

    private static String string(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    public static String auctionUuid() {
        return String.format("%s-%s",
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")),
            string(10));
    }

    public static String subscriberUuid() {
        return UUID.randomUUID().toString();
    }

    public static String sellerUuid() {
        return UUID.randomUUID().toString();
    }

    public static String sellerHandle() {
        return String.format("@user-%s", string(9));
    }
}
