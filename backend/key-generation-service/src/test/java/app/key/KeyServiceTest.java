package app.key;

import app.IntegrationTest;
import app.entity.KeyEntity;
import core.framework.inject.Inject;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class KeyServiceTest extends IntegrationTest {
    @Inject
    KeyService keyService;

    @Test
    void key() {
        int counter = 0;
        List<KeyEntity> keyEntities = new ArrayList<>();
        keyEntities.add(entity("603525fbf5e41e5fa75b814b", "www.google.com", ++counter));
        keyEntities.add(entity("603525fbf5e41e5fa75b814c", "www.facebook.com", ++counter));

        assertThat(keyService.key(keyEntities)).isEqualTo(new String[]{
            "{\"id\":\"603525fbf5e41e5fa75b814b\",\"value\":\"www.google.com\"}",
            "{\"id\":\"603525fbf5e41e5fa75b814c\",\"value\":\"www.facebook.com\"}"
        });
    }

    KeyEntity entity(String id, String url, int incrementalKey) {
        var entity = new KeyEntity();
        entity.id = new ObjectId(id);
        entity.length = 6;
        entity.url = url;
        entity.incrementalKey = incrementalKey;
        return entity;
    }
}
