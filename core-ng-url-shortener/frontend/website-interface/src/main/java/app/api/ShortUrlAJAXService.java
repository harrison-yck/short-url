package app.api;

import app.api.shorturl.EncodeUrlAJAXRequest;
import app.api.shorturl.EncodeUrlAJAXResponse;
import core.framework.api.web.service.POST;
import core.framework.api.web.service.Path;

public interface ShortUrlAJAXService {
    @POST
    @Path("/ajax/encode")
    EncodeUrlAJAXResponse encode(EncodeUrlAJAXRequest request);
}
