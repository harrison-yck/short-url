package app.tool;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Encoder;

import static java.util.Base64.*;

public class ShortUrlService {
    private static final Encoder ENCODER = getUrlEncoder().withoutPadding();
    private static final Decoder DECODER = getUrlDecoder();

    public String encode(String rawUrl) {
        return ENCODER.encodeToString(rawUrl.getBytes(StandardCharsets.UTF_8));
    }

    public String decode(String encodedUrl) {
        return new String(DECODER.decode(encodedUrl.getBytes(StandardCharsets.UTF_8)));
    }
}
