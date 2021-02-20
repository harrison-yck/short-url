package app;

import app.api.KeyGenerationWebService;
import app.api.url.kafka.GenerateUrlCommand;
import app.api.url.kafka.KeyServiceTopic;
import app.kafka.GenerateUrlCommandHandler;
import app.web.KeyGenerationWebServiceImpl;
import core.framework.module.App;

public class KeyGenerationServiceApp extends App {
    @Override
    protected void initialize() {
        api().service(KeyGenerationWebService.class, bind(KeyGenerationWebServiceImpl.class));

        kafka().subscribe(KeyServiceTopic.GENERATE_URL_REQUEST, GenerateUrlCommand.class, bind(GenerateUrlCommandHandler.class));
    }
}
