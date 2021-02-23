package app.service;

import app.IntegrationTest;
import app.api.url.EncodeUrlRequest;
import app.entity.ShortUrlEntity;
import core.framework.inject.Inject;
import core.framework.mongo.Mongo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ShortUrlServiceTest extends IntegrationTest {
    @Inject
    ShortUrlService shortUrlService;
    @Inject
    Mongo mongo;

    @AfterEach
    void clear() {
        mongo.dropCollection("short-url-entity");
    }

    @Test
    void same() {
        var request = new EncodeUrlRequest();
        request.url = "google.com";

        ShortUrlEntity shortUrlEntity = shortUrlService.getEncodedUrl(request);
        assertThat(shortUrlService.findUrl(shortUrlEntity.encodedUrl)).get().satisfies(decodedUrl -> assertThat(decodedUrl).isEqualTo(request.url));
    }
}
