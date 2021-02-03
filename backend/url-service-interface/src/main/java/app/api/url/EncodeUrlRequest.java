package app.api.url;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

public class EncodeUrlRequest {
    @NotNull
    @Property(name = "url")
    public String url;

    @NotNull
    @Property(name = "lastForDays")
    public Integer lastForDays;
}
