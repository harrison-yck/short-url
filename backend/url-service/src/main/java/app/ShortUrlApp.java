package app;

import app.api.UrlWebService;
import app.api.url.ResolveUrlResponse;
import app.api.url.kafka.ClearUrlRecordCommand;
import app.api.url.kafka.UrlServiceTopic;
import app.entity.ShortUrlEntity;
import app.kafka.ClearUrlRecordCommandHandler;
import app.tool.ShortUrlService;
import app.web.UrlWebServiceImpl;
import core.framework.module.App;
import core.framework.module.SystemModule;
import core.framework.mongo.module.MongoConfig;

import java.time.Duration;

public class ShortUrlApp extends App {
    @Override
    protected void initialize() {
        load(new SystemModule("sys.properties"));

        cache().add(ResolveUrlResponse.class, Duration.ofHours(1));

        MongoConfig mongo = config(MongoConfig.class);
        mongo.uri("mongodb://localhost:27017/test");
        mongo.collection(ShortUrlEntity.class);

        bind(ShortUrlService.class);

        kafka().subscribe(UrlServiceTopic.CLEAR_URL_RECORD_REQUEST, ClearUrlRecordCommand.class, bind(ClearUrlRecordCommandHandler.class));
        api().service(UrlWebService.class, bind(UrlWebServiceImpl.class));
    }
}
