package dev.tildejustin.infinity_lite;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class DimensionHashHelper {
    @SuppressWarnings("UnstableApiUsage")
    public static int getHash(String string) {
        return Hashing.sha256().hashString(string + ":why_so_salty#LazyCrypto", StandardCharsets.UTF_8).asInt() & 2147483647;
    }
}
