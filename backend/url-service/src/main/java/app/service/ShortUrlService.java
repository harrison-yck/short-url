package app.service;

import app.api.KeyGenerationWebService;
import app.api.url.EncodeUrlRequest;
import app.entity.ShortUrlEntity;
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
    @Inject
    MongoCollection<ShortUrlEntity> collection;
    @Inject
    KeyGenerationWebService keyGenerationWebService;

    public ShortUrlEntity getEncodedUrl(EncodeUrlRequest request) {
        var entity = new ShortUrlEntity();
        entity.id = new ObjectId();
        entity.originalUrl = request.url;
        entity.encodedUrl = getEncodedUrl();
        entity.createdTime = ZonedDateTime.now();
        collection.insert(entity);
        return entity;
    }

    private String getEncodedUrl() {
        return keyGenerationWebService.getKey();
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
