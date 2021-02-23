package app.entity;

import core.framework.api.validate.NotNull;
import core.framework.mongo.Collection;
import core.framework.mongo.Field;
import core.framework.mongo.Id;
import org.bson.types.ObjectId;

@Collection(name = "key-entity")
public class KeyEntity {
    @Id
    public ObjectId id;

    @NotNull
    @Field(name = "length")
    public Integer length;

    @NotNull
    @Field(name = "incremental_key")
    public Integer incrementalKey;

    @Field(name = "url")
    public String url;

    @NotNull
    @Field(name = "used")
    public Boolean used = Boolean.FALSE;
}
