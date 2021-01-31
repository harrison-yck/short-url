package app.website;

import app.website.web.ForwardPageController;
import app.website.web.HomePageController;
import core.framework.module.App;
import core.framework.module.SystemModule;
import core.framework.web.site.WebDirectory;

import java.util.concurrent.TimeUnit;

import static core.framework.http.HTTPMethod.GET;


public class WebsiteApp extends App {
    @Override
    protected void initialize() {
        load(new SystemModule("sys.properties"));
        loadProperties("app.properties");

        http().maxForwardedIPs(3);
        http().gzip();
        site().security();

        http().limitRate().add("encode", 1, 1, TimeUnit.SECONDS);
        http().limitRate().add("resolve", 5, 1, TimeUnit.SECONDS);

        load(new ShortUrlModule());

        var homePageController = new HomePageController(bean(WebDirectory.class));
        http().route(GET, "/", homePageController::index);
        http().route(GET, "/:path(*)", bind(new ForwardPageController()));
    }
}
