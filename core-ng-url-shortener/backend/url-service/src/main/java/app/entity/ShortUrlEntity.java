package app.entity;

import core.framework.mongo.Collection;
import core.framework.mongo.Field;
import core.framework.mongo.Id;
import org.bson.types.ObjectId;

import java.time.ZonedDateTime;

@Collection(name = "url_entity")
public class ShortUrlEntity {
    @Id
    public ObjectId id;

    @Field(name = "encoded_url")
    public String encodedUrl;

    @Field(name = "original_url")
    public String originalUrl;

    @Field(name = "created_time")
    public ZonedDateTime createdTime;
}
