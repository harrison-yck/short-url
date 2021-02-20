package app;

import app.api.UrlWebService;
import app.api.url.ResolveUrlResponse;
import app.entity.ShortUrlEntity;
import app.service.ShortUrlService;
import app.web.UrlWebServiceImpl;
import core.framework.module.App;
import core.framework.module.SystemModule;
import core.framework.mongo.module.MongoConfig;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class ShortUrlApp extends App {
    @Override
    protected void initialize() {
        load(new SystemModule("sys.properties"));

        cache().add(ResolveUrlResponse.class, Duration.ofDays(30));

        MongoConfig mongo = config(MongoConfig.class);
        mongo.uri("mongodb://localhost:27017/shortUrl");
        mongo.collection(ShortUrlEntity.class);

        bind(ShortUrlService.class);

        http().limitRate().add("resolve", 50, 1, TimeUnit.SECONDS);

        api().service(UrlWebService.class, bind(UrlWebServiceImpl.class));
    }
}
