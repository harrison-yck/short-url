package app;

import app.api.KeyGenerationWebService;
import app.web.KeyGenerationWebServiceImpl;
import core.framework.module.App;

public class KeyGenerationServiceApp extends App {
    @Override
    protected void initialize() {
        api().service(KeyGenerationWebService.class, bind(KeyGenerationWebServiceImpl.class));
    }
}
