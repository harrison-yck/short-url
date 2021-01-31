package app.api.url;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

public class EncodeUrlResponse {
    @NotNull
    @Property(name = "result")
    public String result;
}
