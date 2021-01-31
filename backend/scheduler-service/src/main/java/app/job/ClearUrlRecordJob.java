package app.job;

import app.api.url.kafka.ClearUrlRecordCommand;
import core.framework.inject.Inject;
import core.framework.kafka.MessagePublisher;
import core.framework.scheduler.Job;
import core.framework.scheduler.JobContext;

import java.time.ZonedDateTime;

public class ClearUrlRecordJob implements Job {
    @Inject
    MessagePublisher<ClearUrlRecordCommand> publisher;

    @Override
    public void execute(JobContext context) {
        var command = new ClearUrlRecordCommand();
        command.triggeredTime = ZonedDateTime.now();
        publisher.publish(command);
    }
}
