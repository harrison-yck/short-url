package app.key;

import app.IntegrationTest;
import app.entity.KeyEntity;
import core.framework.inject.Inject;
import core.framework.mongo.MongoCollection;

class KeyServiceTest extends IntegrationTest {
    @Inject
    KeyService keyService;
    @Inject
    MongoCollection<KeyEntity> keyEntities;

}
