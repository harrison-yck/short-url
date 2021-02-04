package app.tool;

import app.api.url.EncodeUrlRequest;
import app.entity.ShortUrlEntity;
import com.mongodb.client.model.BsonField;
import com.mongodb.client.model.Filters;
import core.framework.inject.Inject;
import core.framework.mongo.FindOne;
import core.framework.mongo.MongoCollection;
import core.framework.mongo.Query;
import org.bson.types.ObjectId;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class ShortUrlService {
    public static final String NON_AMBIGUOUS_ALPHABET = "23456789bcdfghjkmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ-_";
    public static final int TOTAL_ALPHABET = NON_AMBIGUOUS_ALPHABET.length();

    @Inject
    MongoCollection<ShortUrlEntity> collection;

    public ShortUrlEntity encode(EncodeUrlRequest request) {
        return insert(request.url, request.lastForDays);
    }

    ShortUrlEntity insert(String rawUrl, Integer lastForDays) {
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
            str.insert(0, NON_AMBIGUOUS_ALPHABET.charAt(num % TOTAL_ALPHABET));
            num = num / TOTAL_ALPHABET;
        }
        return str.toString();
    }

    public Optional<String> findUrl(String encodedUrl) {
        var findQuery = new FindOne();
        findQuery.filter = Filters.eq("encoded_url", encodedUrl);

        Optional<ShortUrlEntity> optionalEntity = collection.findOne(findQuery);
        return optionalEntity.map(shortUrlEntity -> shortUrlEntity.originalUrl);
    }

    public List<String> removeExpiredUrl() {
        var query = new Query();
        query.filter = Filters.lte("expiration_time", ZonedDateTime.now());
        List<ShortUrlEntity> shortUrlEntities = collection.find(query);
        collection.bulkDelete(shortUrlEntities.stream().map(entity -> entity.id).collect(Collectors.toList()));
        return shortUrlEntities.stream().map(entity -> entity.encodedUrl).collect(Collectors.toList());
    }
}
