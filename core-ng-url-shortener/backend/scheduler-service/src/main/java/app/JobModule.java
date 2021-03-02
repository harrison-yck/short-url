package app;

import app.api.url.kafka.GenerateUrlCommand;
import app.api.url.kafka.KeyServiceTopic;
import app.job.GenerateUrlJob;
import core.framework.module.Module;

import java.time.LocalTime;

public class JobModule extends Module {
    @Override
    protected void initialize() {
        kafka().publish(KeyServiceTopic.GENERATE_URL_REQUEST, GenerateUrlCommand.class);
        schedule().dailyAt("generate-url-job", bind(GenerateUrlJob.class), LocalTime.MIDNIGHT);
        // TODO: check key service redis key left, and trigger load to redis if not much left
    }
}