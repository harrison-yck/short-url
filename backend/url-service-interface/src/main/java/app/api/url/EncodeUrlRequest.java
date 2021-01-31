package app.api.url;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

import java.time.MonthDay;
import java.time.ZonedDateTime;

public class EncodeUrlRequest {
    @NotNull
    @Property(name = "url")
    public String url;

    @NotNull
    @Property(name = "lastForDays")
    public Integer lastForDays;
}
