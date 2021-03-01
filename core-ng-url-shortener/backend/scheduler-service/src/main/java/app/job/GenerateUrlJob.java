package app.job;

import app.api.url.kafka.GenerateUrlCommand;
import core.framework.inject.Inject;
import core.framework.kafka.MessagePublisher;
import core.framework.scheduler.Job;
import core.framework.scheduler.JobContext;

import java.time.ZonedDateTime;

public class GenerateUrlJob implements Job {
    @Inject
    MessagePublisher<GenerateUrlCommand> publisher;

    @Override
    public void execute(JobContext context) {
        var command = new GenerateUrlCommand();
        command.triggeredTime = ZonedDateTime.now();
        publisher.publish(command);
    }
}
