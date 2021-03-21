package app.api;

import app.api.url.kafka.GetKeyResponse;
import core.framework.api.web.service.GET;
import core.framework.api.web.service.Path;

public interface KeyGenerationWebService {
    @GET
    @Path("/key")
    GetKeyResponse getKey();

    @GET
    @Path("/generate")
    void generate();
}
