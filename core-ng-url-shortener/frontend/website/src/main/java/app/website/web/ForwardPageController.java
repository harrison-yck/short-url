package app.website.web;

import app.api.UrlWebService;
import app.api.url.ResolveUrlRequest;
import app.api.url.ResolveUrlResponse;
import core.framework.cache.Cache;
import core.framework.http.ContentType;
import core.framework.inject.Inject;
import core.framework.template.HTMLTemplateEngine;
import core.framework.web.Controller;
import core.framework.web.Request;
import core.framework.web.Response;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public class ForwardPageController implements Controller {
    private static final String FAILED_TEMPLATE = "template/invalid.html";
    private static final String FORWARD_TEMPLATE = "template/forward.html";

    @Inject
    HTMLTemplateEngine htmlTemplateEngine;
    @Inject
    Cache<ResolveUrlResponse> resolveUrlResponseCache;
    @Inject
    UrlWebService urlWebService;

    @Override
    public Response execute(Request request) throws URISyntaxException {
        String encodedUrl = request.pathParam("url");

        ResolveUrlResponse resolveUrlResponse = resolveUrlResponseCache.get(encodedUrl, key -> {
            var resolveRequest = new ResolveUrlRequest();
            resolveRequest.url = encodedUrl;
            return urlWebService.resolve(resolveRequest); // maybe not in db, need to evict this cache when encodedUrl is inserted to db
        });

        var action = new ForwardAction();
        action.action = "0; url=" + redirectUrl(resolveUrlResponse.result);

        return resolveUrlResponse.result == null
                ? Response.bytes(htmlTemplateEngine.process(FAILED_TEMPLATE, action).getBytes(StandardCharsets.UTF_8)).contentType(ContentType.TEXT_HTML)
                : Response.bytes(htmlTemplateEngine.process(FORWARD_TEMPLATE, action).getBytes(StandardCharsets.UTF_8)).contentType(ContentType.TEXT_HTML);
    }

    private String redirectUrl(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }
}
