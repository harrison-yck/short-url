package app.key;

import app.api.url.kafka.GetKeyResponse;
import app.entity.KeyEntity;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import core.framework.inject.Inject;
import core.framework.log.Markers;
import core.framework.mongo.Count;
import core.framework.mongo.FindOne;
import core.framework.mongo.MongoCollection;
import core.framework.mongo.Query;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class KeyService {
    private static final int KEY_LENGTH = 6;
    private static final long MAX_KEY = (int) Math.pow(62, KEY_LENGTH);

    private static final double GENERATE_THRESHOLD = 0.9;
    private static final int KEY_BATCH_SIZE = 1000000;

    private final Logger logger = LoggerFactory.getLogger(KeyService.class);

    @Inject
    MongoCollection<KeyEntity> keyEntities;

    public boolean generateKeys() {
        if (keyInDBAlmostEmpty()) {
            var query = new Query();
            query.filter = Filters.eq("length", KEY_LENGTH);
            query.sort = Sorts.descending("incrementalKey");
            query.limit = 1;

            List<KeyEntity> lastKey = keyEntities.find(query);
            long start = lastKey.isEmpty() ? 1 : lastKey.get(0).incrementalKey + 1;
            long end = lastKey.isEmpty() ? KEY_BATCH_SIZE : start + KEY_BATCH_SIZE;
            double usedPercentage = ((double) MAX_KEY - end) / MAX_KEY;
            if (usedPercentage >= 0.8) logger.warn(Markers.errorCode("KEY_ALMOST_USED_UP"), "{}% of keys has been used, please increment the key length ASAP", usedPercentage * 100);

            generate(start, end);
            return true;
        }

        return false;
    }

    private void generate(long start, long end) {
        var generator = new KeyGenerator();
        List<KeyEntity> entities = Lists.newArrayList();

        for (long i = start; i <= end; i++) {
            var entity = new KeyEntity();
            entity.id = new ObjectId();
            entity.length = KEY_LENGTH;
            entity.incrementalKey = i;
            entity.url = generator.generate(i, KEY_LENGTH);
            entities.add(entity);
        }
        keyEntities.bulkInsert(entities);
    }

    public GetKeyResponse getKey() {
        var findOne = new FindOne();
        findOne.filter = Filters.and(Filters.eq("length", KEY_LENGTH), Filters.eq("used", Boolean.FALSE));
        KeyEntity keyEntity = keyEntities.findOne(findOne).orElseThrow(() -> new Error("No key is left"));
        keyEntities.update(Filters.eq("id", keyEntity.id), Updates.set("used", Boolean.TRUE));

        if (keyInDBAlmostEmpty()) generateKeys();

        var getKeyResponse = new GetKeyResponse();
        getKeyResponse.key = keyEntity.url;
        return getKeyResponse;
    }

    private boolean keyInDBAlmostEmpty() {
        var count = new Count();
        count.filter = Filters.and(Filters.eq("length", KEY_LENGTH), Filters.eq("used", Boolean.FALSE));
        return Long.divideUnsigned(keyEntities.count(count), KEY_BATCH_SIZE) >= GENERATE_THRESHOLD;
    }
}
