package app.service;

import app.api.KeyGenerationWebService;
import app.api.url.EncodeUrlRequest;
import app.entity.ShortUrlEntity;
import com.mongodb.client.model.Filters;
import core.framework.inject.Inject;
import core.framework.mongo.FindOne;
import core.framework.mongo.MongoCollection;

import java.time.ZonedDateTime;
import java.util.Optional;


public class ShortUrlService {
    @Inject
    MongoCollection<ShortUrlEntity> collection;
    @Inject
    KeyGenerationWebService keyGenerationWebService;

    public ShortUrlEntity getEncodedUrl(EncodeUrlRequest request) {
        var entity = new ShortUrlEntity();
        entity.originalUrl = request.url;
        entity.encodedUrl = keyGenerationWebService.getKey().key;
        entity.createdTime = ZonedDateTime.now();
        collection.insert(entity);
        return entity;
    }

    public Optional<String> findUrl(String encodedUrl) {
        var findQuery = new FindOne();
        findQuery.filter = Filters.eq("encoded_url", encodedUrl);

        Optional<ShortUrlEntity> optionalEntity = collection.findOne(findQuery);
        return optionalEntity.map(shortUrlEntity -> shortUrlEntity.originalUrl);
    }
}
