package app.api.url.kafka;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

import java.time.ZonedDateTime;

public class GenerateUrlCommand {
    @NotNull
    @Property(name = "triggeredTime")
    public ZonedDateTime triggeredTime;
}
