package app.key;

import app.api.url.kafka.GenerateUrlCommand;
import app.api.url.kafka.GetKeyResponse;
import app.entity.KeyEntity;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import core.framework.inject.Inject;
import core.framework.kafka.MessagePublisher;
import core.framework.log.Markers;
import core.framework.mongo.Count;
import core.framework.mongo.MongoCollection;
import core.framework.mongo.Query;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.List;

public class KeyService {
    private static final int KEY_LENGTH = 6;
    private static final long MAX_KEY = (int) Math.pow(62, KEY_LENGTH);
    static final double GENERATE_THRESHOLD = 0.9;
    static final int KEY_BATCH_SIZE = (int) 1e5;

    private final Logger logger = LoggerFactory.getLogger(KeyService.class);

    @Inject
    MongoCollection<KeyEntity> keyEntities;
    @Inject
    MessagePublisher<GenerateUrlCommand> publisher;

    public void generateKeys() {
        if (needToGenerateKey()) {
            var query = new Query();
            query.filter = Filters.eq("length", KEY_LENGTH);
            query.sort = Sorts.descending("incrementalKey");
            query.limit = 1;

            List<KeyEntity> lastKey = keyEntities.find(query);
            long start = lastKey.isEmpty() ? 1 : lastKey.get(0).incrementalKey + 1;
            long end = lastKey.isEmpty() ? KEY_BATCH_SIZE : start + KEY_BATCH_SIZE;
            double usedPercentage = (double) end / MAX_KEY;
            if (usedPercentage >= 0.8) logger.warn(Markers.errorCode("KEY_ALMOST_USED_UP"), "{}% of keys has been used, please increment the key length ASAP", usedPercentage * 100);

            generate(start, end);
        }
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
        // framework doesn't implement FindOneAndUpdateOptions,
        // so use aggregate sample to randomly pick one to reduce the chance of picking same entity
        // and update the value using compare-and-set manner (used = TRUE) to avoid race condition
//        var aggregate = new Aggregate<KeyEntity>();
//        aggregate.resultClass = KeyEntity.class;
//        aggregate.pipeline = List.of(Aggregates.match(Filters.and(Filters.eq("length", KEY_LENGTH), Filters.eq("used", Boolean.FALSE))), Aggregates.sample(1));
//
//        List<KeyEntity> keyEntities = this.keyEntities.aggregate(aggregate);
//        if (keyEntities.isEmpty()) {
//            publishGenerateKey();
//            return new GetKeyResponse();
//        }

        var query = new Query();
        query.filter = Filters.and(Filters.eq("length", KEY_LENGTH), Filters.eq("used", Boolean.FALSE));

        try {
            List<KeyEntity> keyEntities = this.keyEntities.find(query);
            if (keyEntities.isEmpty()) {
                publishGenerateKey();
                return new GetKeyResponse();
            }

            KeyEntity keyEntity = keyEntities.get(0);
            this.keyEntities.update(Filters.and(Filters.eq("id", keyEntity.id), Filters.eq("used", Boolean.FALSE)), Updates.set("used", Boolean.TRUE));

            if (needToGenerateKey()) publishGenerateKey();

            var getKeyResponse = new GetKeyResponse();
            getKeyResponse.key = keyEntity.url;
            return getKeyResponse;
        } catch (Exception ex) {
            var response = new GetKeyResponse();
            response.key = ex.getMessage();
            return response;
        }
    }

    boolean needToGenerateKey() {
        var count = new Count();
        count.filter = Filters.and(Filters.eq("length", KEY_LENGTH), Filters.eq("used", Boolean.FALSE));

        long numOfUsedRecords = keyEntities.count(count);
        double usedPercentage = (double) numOfUsedRecords / KEY_BATCH_SIZE;
        return numOfUsedRecords == 0 || usedPercentage >= GENERATE_THRESHOLD;
    }

    private void publishGenerateKey() {
        var command = new GenerateUrlCommand();
        command.triggeredTime = ZonedDateTime.now();
        publisher.publish(command);
    }
}
