package app.tool;

import app.IntegrationTest;
import app.api.url.EncodeUrlRequest;
import app.entity.ShortUrlEntity;
import core.framework.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ShortUrlServiceTest extends IntegrationTest {
    @Inject
    ShortUrlService shortUrlService;

    @Test
    void same() {
        var request = new EncodeUrlRequest();
        request.url = "google.com";
        request.lastForDays = 7;

        ShortUrlEntity shortUrlEntity = shortUrlService.encode(request);
        assertThat(shortUrlService.decode(shortUrlEntity.encodedUrl)).get().satisfies(decodedUrl -> assertThat(decodedUrl).isEqualTo(request.url));
    }
}
