package app.web;

import app.api.UrlWebService;
import app.api.url.EncodeUrlRequest;
import app.api.url.EncodeUrlResponse;
import app.api.url.ResolveUrlRequest;
import app.api.url.ResolveUrlResponse;
import app.entity.ShortUrlEntity;
import app.tool.ShortUrlService;
import core.framework.cache.Cache;
import core.framework.inject.Inject;
import core.framework.util.Strings;
import core.framework.web.rate.LimitRate;

public class UrlWebServiceImpl implements UrlWebService {
    @Inject
    ShortUrlService shortUrlService;
    @Inject
    Cache<ResolveUrlResponse> resolveUrlResponseCache;

    @Override
    public EncodeUrlResponse encode(EncodeUrlRequest request) {
        return toResponse(shortUrlService.encode(request));
    }

    private EncodeUrlResponse toResponse(ShortUrlEntity shortUrlEntity) {
        var response = new EncodeUrlResponse();

        if (Strings.isBlank(shortUrlEntity.encodedUrl)) return null;
        response.encodedUrl = shortUrlEntity.encodedUrl;
        response.originalUrl = shortUrlEntity.originalUrl;
        response.createdTime = shortUrlEntity.createdTime;
        response.expirationTime = shortUrlEntity.expirationTime;
        return response;
    }

    @LimitRate("resolve")
    @Override
    public ResolveUrlResponse resolve(ResolveUrlRequest request) {
        return resolveUrlResponseCache.get(request.url, key -> {
            var response = new ResolveUrlResponse();
            response.result = shortUrlService.findUrl(key).isPresent() ? shortUrlService.findUrl(key).get() : null;
            return response;
        });
    }
}
