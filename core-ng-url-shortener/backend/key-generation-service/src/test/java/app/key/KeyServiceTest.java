package app.key;

import app.IntegrationTest;
import app.entity.KeyEntity;
import core.framework.inject.Inject;
import core.framework.mongo.Count;
import core.framework.mongo.MongoCollection;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class KeyServiceTest extends IntegrationTest {
    @Inject
    KeyService keyService;
    @Inject
    MongoCollection<KeyEntity> keyEntities;

    @Test
    void noKey() {
        assertTrue(keyService.needToGenerateKey());
    }

    @Test
    @SuppressWarnings("unchecked")
    void aboveThreshold() {
        keyService.keyEntities = (MongoCollection<KeyEntity>) mock(MongoCollection.class);
        when(keyService.keyEntities.count(any(Count.class))).thenReturn((long) (KeyService.KEY_BATCH_SIZE * KeyService.GENERATE_THRESHOLD + 1));

        assertTrue(keyService.needToGenerateKey());
    }

    @Test
    void belowThreshold() {
        var entity = new KeyEntity();
        entity.id = new ObjectId();
        entity.length = 6;
        entity.incrementalKey = 1L;
        keyEntities.insert(entity);

        assertFalse(keyService.needToGenerateKey());
    }

}
