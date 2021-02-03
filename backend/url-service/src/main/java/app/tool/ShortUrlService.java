package app.tool;

import app.api.url.EncodeUrlRequest;
import app.entity.ShortUrlEntity;
import com.mongodb.client.model.Filters;
import core.framework.inject.Inject;
import core.framework.mongo.MongoCollection;
import core.framework.mongo.Query;
import org.bson.types.ObjectId;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class ShortUrlService {
    public static final String ALPHABET = "23456789bcdfghjkmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ-_";
    public static final int BASE = ALPHABET.length();

    @Inject
    MongoCollection<ShortUrlEntity> collection;

    public ShortUrlEntity encode(EncodeUrlRequest request) {
        return insert(request.url, request.lastForDays);
    }

    private ShortUrlEntity insert(String rawUrl, Integer lastForDays) {
        var entity = new ShortUrlEntity();
        entity.id = new ObjectId();
        entity.originalUrl = rawUrl;
        entity.encodedUrl = encode(entity.id.hashCode());
        entity.createdTime = ZonedDateTime.now();
        entity.expirationTime = entity.createdTime.plusDays(lastForDays);
        collection.insert(entity);

        return entity;
    }

    private String encode(int num) {
        StringBuilder str = new StringBuilder();
        while (num > 0) {
            str.insert(0, ALPHABET.charAt(num % BASE));
            num = num / BASE;
        }
        return str.toString();
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
