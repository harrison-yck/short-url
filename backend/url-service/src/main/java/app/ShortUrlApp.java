package app;

import app.api.url.kafka.ClearUrlRecordCommand;
import app.api.url.kafka.UrlServiceTopic;
import app.kafka.ClearUrlRecordCommandHandler;
import app.tool.ShortUrlService;
import core.framework.module.App;
import core.framework.module.SystemModule;

import java.time.Duration;

public class ShortUrlApp extends App {
    @Override
    protected void initialize() {
        load(new SystemModule("sys.properties"));

        bind(ShortUrlService.class);

        cache().add(app.api.ShortUrl.ResolveUrlAJAXResponse.class, Duration.ofHours(1));

        kafka().subscribe(UrlServiceTopic.CLEAR_URL_RECORD_REQUEST, ClearUrlRecordCommand.class, bind(ClearUrlRecordCommandHandler.class));
    }
}
