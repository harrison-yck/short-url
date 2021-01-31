package app.website;

import app.api.ShortUrlAJAXService;
import app.api.UrlWebService;
import app.website.shorturl.ShortUrlAJAXServiceImpl;
import core.framework.module.Module;

public class ShortUrlModule extends Module {
    @Override
    protected void initialize() {
        api().client(UrlWebService.class, requiredProperty("app.urlServiceURL"));
        api().service(ShortUrlAJAXService.class, bind(ShortUrlAJAXServiceImpl.class));
    }
}
