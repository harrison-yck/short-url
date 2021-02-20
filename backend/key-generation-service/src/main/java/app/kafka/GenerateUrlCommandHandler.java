package app.kafka;

import app.api.url.kafka.GenerateUrlCommand;
import app.key.KeyService;
import core.framework.inject.Inject;
import core.framework.kafka.MessageHandler;


public class GenerateUrlCommandHandler implements MessageHandler<GenerateUrlCommand> {
    @Inject
    KeyService keyService;

    @Override
    public void handle(String key, GenerateUrlCommand command) {
        keyService.generateKeys();
    }
}
