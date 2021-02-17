package app.service;

import app.IntegrationTest;
import app.api.url.EncodeUrlRequest;
import app.entity.ShortUrlEntity;
import core.framework.inject.Inject;
import core.framework.mongo.Count;
import core.framework.mongo.Mongo;
import core.framework.mongo.MongoCollection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ShortUrlServiceTest extends IntegrationTest {
    @Inject
    ShortUrlService shortUrlService;
    @Inject
    MongoCollection<ShortUrlEntity> collection;
    @Inject
    Mongo mongo;

    @AfterEach
    void clear() {
        mongo.dropCollection("short-url-entity");
    }

    @Disabled
    @Test
    void same() {
        var request = new EncodeUrlRequest();
        request.url = "google.com";

        ShortUrlEntity shortUrlEntity = shortUrlService.getEncodedUrl(request);
        assertThat(shortUrlService.findUrl(shortUrlEntity.encodedUrl)).get().satisfies(decodedUrl -> assertThat(decodedUrl).isEqualTo(request.url));
    }

    @Disabled
    @Test
    void removeUrl() {

    }

    private int count() {
        return (int) collection.count(new Count());
    }
}
