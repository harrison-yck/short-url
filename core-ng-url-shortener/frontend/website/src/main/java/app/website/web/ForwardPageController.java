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

import java.nio.file.Path;

public class ForwardPageController implements Controller {
    private final Path failedTemplate;

    public ForwardPageController(WebDirectory webDirectory) {
        this.failedTemplate = webDirectory.path("/template/index.html");
    }

    @Inject
    Cache<ResolveUrlResponse> resolveUrlResponseCache;
    @Inject
    UrlWebService urlWebService;

    @Override
    public Response execute(Request request) {
        String encodedUrl = request.pathParam("url");

        ResolveUrlResponse resolveUrlResponse = resolveUrlResponseCache.get(encodedUrl, key -> {
            var resolveRequest = new ResolveUrlRequest();
            resolveRequest.url = encodedUrl;
            return urlWebService.resolve(resolveRequest); // maybe not in db, need to evict this cache when encodedUrl is inserted to db
        });

        var action = new ForwardAction();
        action.action = "0; url=" + redirectUrl(resolveUrlResponse.result);

        return resolveUrlResponse.result == null
                ? Response.bytes(Files.bytes(failedTemplate)).contentType(ContentType.TEXT_HTML)
                : Response.redirect(redirectUrl(resolveUrlResponse.result));
    }

    private String redirectUrl(String url) {
        return url.startsWith("www.") ? url.substring(4) : url;
    }
}
