package app.web;

import app.api.KeyGenerationWebService;
import app.api.url.kafka.GetKeyResponse;
import app.key.KeyService;
import core.framework.inject.Inject;

public class KeyGenerationWebServiceImpl implements KeyGenerationWebService {
    @Inject
    KeyService keyService;

    @Override
    public GetKeyResponse getKey() {
        return keyService.getKey();
    }
}
