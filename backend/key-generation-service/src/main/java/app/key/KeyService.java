package app.key;

import core.framework.inject.Inject;
import core.framework.redis.Redis;

public class KeyService {
    @Inject
    Redis redis;

    public void generateKeys() {
        redis.list().push();
    }

    public String getKey() {
        
    }
}
