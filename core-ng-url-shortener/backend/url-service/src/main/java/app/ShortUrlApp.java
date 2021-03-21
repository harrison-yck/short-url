package app;

import app.api.KeyGenerationWebService;
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
        loadProperties("app.properties");

        cache().add(ResolveUrlResponse.class, Duration.ofDays(30));

        MongoConfig mongo = config(MongoConfig.class);
        mongo.uri("mongodb://mongo-db:27017/url");
        mongo.collection(ShortUrlEntity.class);

        api().client(KeyGenerationWebService.class, requiredProperty("app.keyServiceURL"));

        bind(ShortUrlService.class);

        http().limitRate().add("resolve", 50, 10, TimeUnit.SECONDS);

        api().service(UrlWebService.class, bind(UrlWebServiceImpl.class));
    }
}
