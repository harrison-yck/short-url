package app;

import app.api.url.kafka.ClearUrlRecordCommand;
import app.api.url.kafka.UrlServiceTopic;
import app.job.ClearUrlRecordJob;
import core.framework.module.Module;

import java.time.LocalTime;

public class JobModule extends Module {
    @Override
    protected void initialize() {
        kafka().publish(UrlServiceTopic.CLEAR_URL_RECORD_REQUEST, ClearUrlRecordCommand.class);
        schedule().dailyAt("clear-url-record-job", bind(ClearUrlRecordJob.class), LocalTime.MIDNIGHT);
    }
}
