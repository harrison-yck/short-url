package app.key;

import app.api.url.kafka.GenerateUrlCommand;
import app.entity.KeyEntity;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;
import core.framework.inject.Inject;
import core.framework.json.Bean;
import core.framework.kafka.MessagePublisher;
import core.framework.mongo.MongoCollection;
import core.framework.mongo.Query;
import core.framework.redis.Redis;
import core.framework.util.Strings;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;

import java.time.ZonedDateTime;
import java.util.List;

public class KeyService {
    private static final String URL_KEY = "KEY:{}";
    private static final int KEY_LENGTH = 6;
    private static final int KEY_BATCH_SIZE = 100000;

    @Inject
    MongoCollection<KeyEntity> keyEntities;
    @Inject
    Redis redis;
    @Inject
    MessagePublisher<GenerateUrlCommand> publisher;

    public void generateKeys() {
        var query = new Query();
        query.filter = Filters.eq("length", KEY_LENGTH);
        query.sort = Sorts.descending("incrementalKey");
        query.limit = 1;

        List<KeyEntity> lastKey = keyEntities.find(query);
        int start = lastKey.isEmpty() ? 1 : lastKey.get(0).incrementalKey + 1;
        int end = lastKey.isEmpty() ? KEY_BATCH_SIZE : start + KEY_BATCH_SIZE;

        generate(start, end);
    }

    void generate(int start, int end) {
        var generator = new KeyGenerator();
        List<KeyEntity> entities = Lists.newArrayList();

        for (int i = start; i < end; i++) {
            var entity = new KeyEntity();
            entity.id = new ObjectId();
            entity.length = KEY_LENGTH;
            entity.incrementalKey = i;
            entity.url = generator.generate(KEY_LENGTH, i);
            entities.add(entity);
        }
        keyEntities.bulkInsert(entities);
    }

    public String getKey() {
        String object = redis.list().pop(Strings.format(URL_KEY, KEY_LENGTH));

        if (object != null) {
            Key key = Bean.fromJSON(Key.class, object);
            keyEntities.update(Filters.eq("id", key.id), Updates.set("used", Boolean.TRUE));
            return key.value;
        } else {
            loadKeyToRedis();
            throw new Error();
        }
    }

    private void loadKeyToRedis() {
        var query = new Query();
        query.filter = Filters.and(Filters.eq("length", KEY_LENGTH), Filters.eq("used", Boolean.FALSE));
        redis.list().push(Strings.format(URL_KEY, KEY_LENGTH), key(keyEntities.find(query)));

        var command = new GenerateUrlCommand();
        command.triggeredTime = ZonedDateTime.now();
        publisher.publish(command);
    }

    String[] key(List<KeyEntity> entities) {
        return entities.stream().map(entity -> {
            var key = new Key();
            key.id = entity.id;
            key.value = entity.url;
            return Bean.toJSON(key);
        }).toArray(String[]::new);
    }

    static class Key {
        @NotNull
        @Property(name = "id")
        public ObjectId id;

        @NotNull
        @Property(name = "value")
        public String value;
    }
}
