package com.messenger.messengerservice.utils;

import io.jsonwebtoken.io.Decoders;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class Base64DecoderUtils {

    public static byte[] decodeBase64(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        char[] buf = new char[4096];

        try (Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
            int read;
            while ((read = reader.read(buf)) >= 0) {
                sb.append(buf, 0, read);
            }
        }

        return Decoders.BASE64.decode(sb.toString());
    }
}
