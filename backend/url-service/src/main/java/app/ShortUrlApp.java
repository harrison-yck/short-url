package app;

import app.api.UrlWebService;
import app.api.url.kafka.ClearUrlRecordCommand;
import app.api.url.kafka.UrlServiceTopic;
import app.entity.ShortUrlEntity;
import app.kafka.ClearUrlRecordCommandHandler;
import app.tool.ShortUrlService;
import app.web.UrlWebServiceImpl;
import core.framework.module.App;
import core.framework.module.SystemModule;

public class ShortUrlApp extends App {
    @Override
    protected void initialize() {
        load(new SystemModule("sys.properties"));

        bind(ShortUrlService.class);

        kafka().subscribe(UrlServiceTopic.CLEAR_URL_RECORD_REQUEST, ClearUrlRecordCommand.class, bind(ClearUrlRecordCommandHandler.class));

        api().service(UrlWebService.class, bind(UrlWebServiceImpl.class));
    }
}
