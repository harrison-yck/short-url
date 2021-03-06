package app.website;

import app.website.web.ForwardAction;
import app.website.web.ForwardPageController;
import app.website.web.HomePageController;
import core.framework.module.App;
import core.framework.module.SystemModule;
import core.framework.template.HTMLTemplateEngine;
import core.framework.web.site.WebDirectory;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static core.framework.http.HTTPMethod.GET;


public class WebsiteApp extends App {
    @Override
    protected void initialize() {
        load(new SystemModule("sys.properties"));
        loadProperties("app.properties");

        load(new ShortUrlModule());

        http().gzip();
        site().security();

        HTMLTemplateEngine engine = bind(HTMLTemplateEngine.class);
        engine.add("template/forward.html", ForwardAction.class);

        var homePageController = new HomePageController(bean(WebDirectory.class));
        http().route(GET, "/", homePageController::index);
        http().route(GET, "/:url", bind(new ForwardPageController(bean(WebDirectory.class))));

        site().staticContent("/static").cache(Duration.ofHours(1));
        site().staticContent("/favicon.ico").cache(Duration.ofHours(1));

        http().limitRate().add("encode", 1, 1, TimeUnit.SECONDS);
    }
}
