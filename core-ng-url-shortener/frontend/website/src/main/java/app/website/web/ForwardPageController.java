package app.website.web;

import app.api.UrlWebService;
import app.api.url.ResolveUrlRequest;
import app.api.url.ResolveUrlResponse;
import core.framework.cache.Cache;
import core.framework.http.ContentType;
import core.framework.inject.Inject;
import core.framework.util.Files;
import core.framework.web.Controller;
import core.framework.web.Request;
import core.framework.web.Response;
import core.framework.web.site.WebDirectory;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class ForwardPageController implements Controller {
    private final Path failedTemplate;

    @Inject
    Cache<ResolveUrlResponse> resolveUrlResponseCache;
    @Inject
    UrlWebService urlWebService;

    public ForwardPageController(WebDirectory webDirectory) {
        failedTemplate = webDirectory.path("/template/invalid.html");
    }

    @Override
    public Response execute(Request request) throws URISyntaxException {
        String encodedUrl = request.pathParam("url");

        ResolveUrlResponse resolveUrlResponse = resolveUrlResponseCache.get(encodedUrl, key -> {
            var resolveRequest = new ResolveUrlRequest();
            resolveRequest.url = encodedUrl;
            return urlWebService.resolve(resolveRequest); // maybe not in db, need to evict this cache when encodedUrl is inserted to db
        });

        return resolveUrlResponse.result == null
                ? Response.bytes(Files.bytes(failedTemplate)).contentType(ContentType.TEXT_HTML)
                : Response.redirect(redirectUrl(resolveUrlResponse.result));
    }

    private String redirectUrl(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }
}
