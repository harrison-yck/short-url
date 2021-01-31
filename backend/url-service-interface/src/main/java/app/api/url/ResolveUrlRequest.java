package app.api.url;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;
import core.framework.api.web.service.QueryParam;

public class ResolveUrlRequest {
    @NotNull
    @QueryParam(name = "url")
    public String url;
}
