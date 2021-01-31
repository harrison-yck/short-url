package app.api.shorturl;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

public class EncodeUrlAJAXRequest {
    @NotNull
    @Property(name = "url")
    public String url;

    @NotNull
    @Property(name = "availability")
    public Availability availability;

    public enum Availability {
        @Property(name = "ONE_DAY")
        ONE_DAY,
        @Property(name = "ONE_WEEK")
        ONE_WEEK
    }
}
