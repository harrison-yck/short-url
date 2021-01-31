package app.api.shorturl;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

public class EncodeUrlAJAXResponse {
    @Property(name = "result")
    public String result;

    @NotNull
    @Property(name = "success")
    public Boolean success;
}
