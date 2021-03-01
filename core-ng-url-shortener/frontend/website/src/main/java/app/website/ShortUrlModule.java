package app.website;

import app.api.ShortUrlAJAXService;
import app.api.UrlWebService;
import app.api.url.ResolveUrlResponse;
import app.website.shorturl.ShortUrlAJAXServiceImpl;
import core.framework.module.Module;

import java.time.Duration;

public class ShortUrlModule extends Module {
    @Override
    protected void initialize() {
        cache().add(ResolveUrlResponse.class, Duration.ofHours(1));

        api().client(UrlWebService.class, requiredProperty("app.urlServiceURL"));
        api().service(ShortUrlAJAXService.class, bind(ShortUrlAJAXServiceImpl.class));
    }
}
