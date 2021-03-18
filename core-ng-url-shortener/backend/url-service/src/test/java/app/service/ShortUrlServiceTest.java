package app.service;

import app.IntegrationTest;
import app.api.KeyGenerationWebService;
import app.api.url.EncodeUrlRequest;
import app.api.url.kafka.GetKeyResponse;
import app.entity.ShortUrlEntity;
import core.framework.inject.Inject;
import core.framework.mongo.Mongo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        var response = new GetKeyResponse();
        response.key = "wDDY0B";
        shortUrlService.keyGenerationWebService = mock(KeyGenerationWebService.class);
        when(shortUrlService.keyGenerationWebService.getKey()).thenReturn(response);

        var request = new EncodeUrlRequest();
        request.url = "google.com";
        ShortUrlEntity shortUrlEntity = shortUrlService.getEncodedUrl(request);

        assertThat(shortUrlService.findUrl(shortUrlEntity.encodedUrl)).get().satisfies(decodedUrl -> assertThat(decodedUrl).isEqualTo(request.url));
    }
}
