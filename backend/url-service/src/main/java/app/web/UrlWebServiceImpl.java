package app.web;

import app.api.UrlWebService;
import app.api.url.EncodeUrlRequest;
import app.api.url.EncodeUrlResponse;
import app.api.url.ResolveUrlRequest;
import app.api.url.ResolveUrlResponse;
import app.tool.ShortUrlService;
import core.framework.inject.Inject;

public class UrlWebServiceImpl implements UrlWebService {
    @Inject
    ShortUrlService shortUrlService;

    @Override
    public EncodeUrlResponse encode(EncodeUrlRequest request) {
        return null;
    }

    @Override
    public ResolveUrlResponse resolve(ResolveUrlRequest request) {
        return null;
    }
}
