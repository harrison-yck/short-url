package app.tool;

import app.IntegrationTest;
import app.api.url.EncodeUrlRequest;
import app.entity.ShortUrlEntity;
import com.mongodb.client.model.Filters;
import core.framework.inject.Inject;
import core.framework.mongo.Count;
import core.framework.mongo.Mongo;
import core.framework.mongo.MongoCollection;
import org.junit.jupiter.api.AfterEach;
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

    @Test
    void same() {
        var request = new EncodeUrlRequest();
        request.url = "google.com";
        request.lastForDays = 7;

        ShortUrlEntity shortUrlEntity = shortUrlService.encode(request);
        assertThat(shortUrlService.findUrl(shortUrlEntity.encodedUrl)).get().satisfies(decodedUrl -> assertThat(decodedUrl).isEqualTo(request.url));
    }

    @Test
    void removeUrl() {
        shortUrlService.insert("google.com", -1);
        shortUrlService.insert("apple.com", 2);
        shortUrlService.insert("google.com", 3);
        assertThat(count()).isEqualTo(3);

        shortUrlService.removeExpiredUrl();
        assertThat(count()).isEqualTo(2);
    }

    private int count() {
        return (int) collection.count(new Count());
    }
}
