package app;

import app.api.KeyGenerationWebService;
import app.api.url.kafka.GenerateUrlCommand;
import app.api.url.kafka.KeyServiceTopic;
import app.entity.KeyEntity;
import app.kafka.GenerateUrlCommandHandler;
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
        mongo.uri("mongodb://mongo-db:27017/key");
        mongo.collection(KeyEntity.class);

        kafka().publish(KeyServiceTopic.GENERATE_URL_REQUEST, GenerateUrlCommand.class);

        bind(KeyService.class);

        kafka().subscribe(KeyServiceTopic.GENERATE_URL_REQUEST, GenerateUrlCommand.class, bind(GenerateUrlCommandHandler.class));

        api().service(KeyGenerationWebService.class, bind(KeyGenerationWebServiceImpl.class));
    }
}
