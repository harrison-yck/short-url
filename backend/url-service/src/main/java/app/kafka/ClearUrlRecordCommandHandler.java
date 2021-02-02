package app.kafka;

import app.api.url.ResolveUrlResponse;
import app.api.url.kafka.ClearUrlRecordCommand;
import app.tool.ShortUrlService;
import core.framework.cache.Cache;
import core.framework.inject.Inject;
import core.framework.kafka.MessageHandler;

import java.util.List;


public class ClearUrlRecordCommandHandler implements MessageHandler<ClearUrlRecordCommand> {
    @Inject
    ShortUrlService shortUrlService;
    @Inject
    Cache<ResolveUrlResponse> resolveUrlResponseCache;


    @Override
    public void handle(String key, ClearUrlRecordCommand command) {
        List<String> expiredUrls = shortUrlService.removeExpiredUrl();
        evictCache(expiredUrls);
    }

    private void evictCache(List<String> expiredUrls) {
        resolveUrlResponseCache.evictAll(expiredUrls);
    }
}
