package app.web;

import app.api.UrlWebService;
import app.api.url.EncodeUrlRequest;
import app.api.url.EncodeUrlResponse;
import app.api.url.ResolveUrlRequest;
import app.api.url.ResolveUrlResponse;
import app.tool.ShortUrlService;
import core.framework.cache.Cache;
import core.framework.inject.Inject;

public class UrlWebServiceImpl implements UrlWebService {
    @Inject
    ShortUrlService shortUrlService;
    @Inject
    Cache<ResolveUrlResponse> resolveUrlResponseCache;

    @Override
    public EncodeUrlResponse encode(EncodeUrlRequest request) {
        return encoded(shortUrlService.encode(request));
    }

    private EncodeUrlResponse encoded(String encodedUrl) {
        var response = new EncodeUrlResponse();
        response.result = encodedUrl;
        return response;
    }

    @Override
    public ResolveUrlResponse resolve(ResolveUrlRequest request) {
        return resolveUrlResponseCache.get(request.url, key -> {
            var response = new ResolveUrlResponse();
            response.result = shortUrlService.decode(key).isPresent() ? shortUrlService.decode(key).get() : null;
            return response;
        });
    }
}
