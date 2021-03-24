package app.website.web;

import app.api.UrlWebService;
import app.api.url.ResolveUrlRequest;
import app.api.url.ResolveUrlResponse;
import core.framework.cache.Cache;
import core.framework.http.ContentType;
import core.framework.inject.Inject;
import core.framework.util.Files;
import core.framework.util.Strings;
import core.framework.web.Controller;
import core.framework.web.Request;
import core.framework.web.Response;
import core.framework.web.site.WebDirectory;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class ForwardPageController implements Controller {
    private final Path failedTemplate;
    private final Path homePage;

    @Inject
    Cache<ResolveUrlResponse> resolveUrlResponseCache;
    @Inject
    UrlWebService urlWebService;

    public ForwardPageController(WebDirectory webDirectory) {
        homePage = webDirectory.path("/template/index.html");
        failedTemplate = webDirectory.path("/template/invalid.html");
    }

    @Override
    public Response execute(Request request) throws URISyntaxException {
        String url = request.pathParam("url");

        if (url == null || Strings.isBlank(url)) Response.bytes(Files.bytes(homePage)).contentType(ContentType.TEXT_HTML);

        var resolveRequest = new ResolveUrlRequest();
        resolveRequest.url = url;

        ResolveUrlResponse resolveUrlResponse = resolveUrlResponseCache.get(resolveRequest.url, key -> {
            var response = new ResolveUrlResponse();
            ResolveUrlResponse resolvedResult = urlWebService.resolve(resolveRequest);
            return resolvedResult == null ? null : response;
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
