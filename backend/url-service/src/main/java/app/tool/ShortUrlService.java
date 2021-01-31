package app.tool;

import app.api.url.EncodeUrlRequest;
import app.entity.ShortUrlEntity;
import com.mongodb.client.model.Filters;
import core.framework.inject.Inject;
import core.framework.mongo.Mongo;
import core.framework.mongo.MongoCollection;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Optional;

import static java.util.Base64.*;

public class ShortUrlService {
    private static final Encoder ENCODER = getUrlEncoder().withoutPadding(); // TODO change to key-based

    @Inject
    MongoCollection<ShortUrlEntity> collection;

    // TODO also include api key to encode
    public String encode(EncodeUrlRequest request) {
        return insert(request.url, request.lastForDays);
    }

    private String insert(String rawUrl, Integer lastForDays) {
        String encodedUrl = ENCODER.encodeToString(rawUrl.getBytes(StandardCharsets.UTF_8));

        var entity = new ShortUrlEntity();
        entity.originalUrl = rawUrl;
        entity.encodedUrl = encodedUrl;
        entity.createdTime = ZonedDateTime.now();
        entity.expirationTime = entity.createdTime.plusDays(lastForDays);
        collection.insert(entity);

        return encodedUrl;
    }

    public Optional<String> decode(String encodedUrl) {
        Optional<ShortUrlEntity> optionalEntity = collection.get(encodedUrl);
        return optionalEntity.map(shortUrlEntity -> shortUrlEntity.originalUrl);
    }
}
