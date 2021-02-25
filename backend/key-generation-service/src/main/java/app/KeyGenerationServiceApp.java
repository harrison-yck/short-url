package app;

import app.api.KeyGenerationWebService;
import app.entity.KeyEntity;
import app.key.KeyService;
import app.web.KeyGenerationWebServiceImpl;
import core.framework.module.App;
import core.framework.module.SystemModule;
import core.framework.mongo.module.MongoConfig;

public class KeyGenerationServiceApp extends App {
    @Override
    protected void initialize() {
        load(new SystemModule("sys.properties"));

        MongoConfig mongo = config(MongoConfig.class);
        mongo.uri("mongodb://localhost:27017/shortUrl");
        mongo.collection(KeyEntity.class);

        bind(KeyService.class);

        api().service(KeyGenerationWebService.class, bind(KeyGenerationWebServiceImpl.class));
    }
}
