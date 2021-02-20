package app.entity;

import core.framework.mongo.Collection;
import core.framework.mongo.Field;
import core.framework.mongo.Id;
import org.bson.types.ObjectId;

@Collection(name = "key-entity")
public class KeyEntity {
    @Id
    public ObjectId id;

    @Field(name = "length")
    public Integer length;

    @Field(name = "incremental_key")
    public Integer incrementalKey;

    @Field(name = "url")
    public String url;

    @Field(name = "used")
    public Boolean used = Boolean.FALSE;
}
