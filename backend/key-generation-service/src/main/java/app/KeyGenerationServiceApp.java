package app;

import app.api.KeyGenerationWebService;
import app.api.url.kafka.GenerateUrlCommand;
import app.api.url.kafka.KeyServiceTopic;
import app.entity.KeyEntity;
import app.kafka.GenerateUrlCommandHandler;
import app.key.Key;
import app.key.KeyService;
import app.web.KeyGenerationWebServiceImpl;
import core.framework.json.Bean;
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

        kafka().publish(KeyServiceTopic.GENERATE_URL_REQUEST, GenerateUrlCommand.class);

        Bean.register(Key.class);
        bind(KeyService.class);

        api().service(KeyGenerationWebService.class, bind(KeyGenerationWebServiceImpl.class));

        kafka().subscribe(KeyServiceTopic.GENERATE_URL_REQUEST, GenerateUrlCommand.class, bind(GenerateUrlCommandHandler.class));
    }
}
