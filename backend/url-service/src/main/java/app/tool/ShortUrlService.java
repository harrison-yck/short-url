package app.tool;

import app.api.url.EncodeUrlRequest;
import app.entity.ShortUrlEntity;
import com.mongodb.client.model.Filters;
import core.framework.inject.Inject;
import core.framework.mongo.MongoCollection;
import core.framework.mongo.Query;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Base64.Encoder;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static core.framework.crypto.Hash.md5Hex;
import static java.util.Base64.getUrlEncoder;

public class ShortUrlService {
    private static final Encoder ENCODER = getUrlEncoder().withoutPadding();

    @Inject
    MongoCollection<ShortUrlEntity> collection;

    public String encode(EncodeUrlRequest request) {
        return insert(request.url, request.randomStr, request.lastForDays);
    }

    private String insert(String rawUrl, String randomStr, Integer lastForDays) {
        if (randomStr == null) return "";

        String concatString = rawUrl + randomStr;
        String encodedUrl = ENCODER.encodeToString(md5Hex(concatString.getBytes(StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8));

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

    public List<String> removeExpiredUrl() {
        var query = new Query();
        query.filter = Filters.lte("expiration_time", ZonedDateTime.now());
        List<String> encodedUrls = collection.find(query).stream().map(entity -> entity.encodedUrl).collect(Collectors.toList());
        collection.bulkDelete(encodedUrls);
        return encodedUrls;
    }
}
