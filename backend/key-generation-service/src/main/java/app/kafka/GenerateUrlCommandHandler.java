package app.kafka;

import app.api.url.kafka.GenerateUrlCommand;
import app.key.KeyService;
import core.framework.inject.Inject;
import core.framework.kafka.MessageHandler;
import core.framework.log.ActionLogContext;


public class GenerateUrlCommandHandler implements MessageHandler<GenerateUrlCommand> {
    @Inject
    KeyService keyService;

    @Override
    public void handle(String key, GenerateUrlCommand command) {
        ActionLogContext.put("triggerTime", command.triggeredTime);

        if (keyService.generateKeys()) {
            ActionLogContext.put("success", true);
        }
        ActionLogContext.put("success", false);
    }
}
