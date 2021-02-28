package app.api.shorturl;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

public class EncodeUrlAJAXRequest {
    @NotNull
    @Property(name = "url")
    public String url;
}
