package app.entity;

import core.framework.mongo.Collection;
import core.framework.mongo.Field;
import core.framework.mongo.Id;

import java.time.ZonedDateTime;

@Collection(name = "short-url-entity")
public class ShortUrlEntity {
    @Id
    public String encodedUrl;

    @Field(name = "original_url")
    public String originalUrl;

    @Field(name = "created_time")
    public ZonedDateTime createdTime;

    @Field(name = "expiration_time")
    public ZonedDateTime expirationTime;
}
