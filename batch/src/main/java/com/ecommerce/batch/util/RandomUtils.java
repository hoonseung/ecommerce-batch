package com.ecommerce.batch.util;

import java.time.Instant;
import java.util.UUID;

public class RandomUtils {

    public static String generateRandomProductId() {
        return Instant.now().toEpochMilli() + "-" + UUID.randomUUID();
    }
}
