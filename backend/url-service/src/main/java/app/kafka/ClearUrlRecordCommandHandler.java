package app.kafka;

import app.api.url.kafka.ClearUrlRecordCommand;
import core.framework.kafka.MessageHandler;


public class ClearUrlRecordCommandHandler implements MessageHandler<ClearUrlRecordCommand> {

    @Override
    public void handle(String key, ClearUrlRecordCommand command) {

//        evictCache();
    }

//    private void evictCache() {
//        resolveUrlAJAXResponseCache.evictAll();
//    }
}
