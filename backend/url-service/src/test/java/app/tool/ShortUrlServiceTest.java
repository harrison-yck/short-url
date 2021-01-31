package app.tool;

import app.IntegrationTest;
import core.framework.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ShortUrlServiceTest extends IntegrationTest {
    @Inject
    ShortUrlService shortUrlService;

    @Test
    void same() {
        String url = "google.com";
        String encodedUrl = shortUrlService.encode(url);
        String decodedUrl = shortUrlService.decode(encodedUrl);
        assertThat(url).isEqualTo(decodedUrl);
    }
}
