package app.api.url;

import core.framework.api.web.service.QueryParam;

public class EncodeUrlRequest {
    @QueryParam(name = "url")
    public String url;
}
