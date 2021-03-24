package app.web;

import app.api.UrlWebService;
import app.api.url.EncodeUrlRequest;
import app.api.url.EncodeUrlResponse;
import app.api.url.ResolveUrlRequest;
import app.api.url.ResolveUrlResponse;
import app.entity.ShortUrlEntity;
import app.service.ShortUrlService;
import core.framework.cache.Cache;
import core.framework.inject.Inject;
import core.framework.util.Strings;
import core.framework.web.rate.LimitRate;

import java.util.Optional;

public class UrlWebServiceImpl implements UrlWebService {
    @Inject
    ShortUrlService shortUrlService;
    @Inject
    Cache<ResolveUrlResponse> resolveUrlResponseCache;

    @Override
    public EncodeUrlResponse encode(EncodeUrlRequest request) {
        return toResponse(shortUrlService.getEncodedUrl(request));
    }

    private EncodeUrlResponse toResponse(ShortUrlEntity shortUrlEntity) {
        if (Strings.isBlank(shortUrlEntity.encodedUrl)) return null;

        var response = new EncodeUrlResponse();
        response.encodedUrl = shortUrlEntity.encodedUrl;
        response.originalUrl = shortUrlEntity.originalUrl;
        response.createdTime = shortUrlEntity.createdTime;

        resolveUrlResponseCache.evict(shortUrlEntity.encodedUrl);
        return response;
    }

    @LimitRate("resolve")
    @Override
    public ResolveUrlResponse resolve(ResolveUrlRequest request) {
        Optional<String> urlOptional = shortUrlService.findUrl(request.url);
        var response = new ResolveUrlResponse();
        response.result = urlOptional.orElse(null);
        return response;
    }
}
