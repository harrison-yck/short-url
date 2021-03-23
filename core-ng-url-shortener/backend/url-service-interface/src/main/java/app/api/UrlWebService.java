package app.api;

import app.api.url.EncodeUrlRequest;
import app.api.url.EncodeUrlResponse;
import app.api.url.ResolveUrlRequest;
import app.api.url.ResolveUrlResponse;
import core.framework.api.web.service.GET;
import core.framework.api.web.service.Path;

public interface UrlWebService {
    @GET
    @Path("/encode")
    EncodeUrlResponse encode(EncodeUrlRequest request);

    @GET
    @Path("/resolve")
    ResolveUrlResponse resolve(ResolveUrlRequest request);
}
