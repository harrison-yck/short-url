package app.api.url.kafka;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

import java.time.ZonedDateTime;

public class ClearUrlRecordCommand {
    @NotNull
    @Property(name = "triggeredTime")
    public ZonedDateTime triggeredTime;
}
