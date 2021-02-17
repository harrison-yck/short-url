package app.web;

import app.api.KeyGenerationWebService;
import app.key.KeyService;
import core.framework.inject.Inject;

public class KeyGenerationWebServiceImpl implements KeyGenerationWebService {
    @Inject
    KeyService keyService;

    @Override
    public String getKey() {
        return keyService.getKey();
    }
}
