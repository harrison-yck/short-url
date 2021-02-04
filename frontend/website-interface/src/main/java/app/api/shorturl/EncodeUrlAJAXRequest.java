package app.api.shorturl;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;
import core.framework.api.validate.Pattern;

public class EncodeUrlAJAXRequest {
    @NotNull
    @Property(name = "url")
    @Pattern("(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})")
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
