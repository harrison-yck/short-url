package app.website.web;

import app.api.UrlWebService;
import app.api.url.ResolveUrlRequest;
import app.api.url.ResolveUrlResponse;
import core.framework.http.ContentType;
import core.framework.inject.Inject;
import core.framework.template.HTMLTemplateEngine;
import core.framework.web.Controller;
import core.framework.web.Request;
import core.framework.web.Response;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ForwardPageController implements Controller {
    private static final String FAILED_TEMPLATE = "/template/invalid.html";

    @Inject
    HTMLTemplateEngine htmlTemplateEngine;
    @Inject
    UrlWebService urlWebService;

    @Override
    public Response execute(Request request) {
        var resolveRequest = new ResolveUrlRequest();
        resolveRequest.url = request.path();
        ResolveUrlResponse resolvedResult = urlWebService.resolve(resolveRequest);

        System.out.println("redirect page");

        if (resolvedResult.result != null) {
            return Response.bytes(htmlTemplateEngine.process(FAILED_TEMPLATE, new Object()).getBytes(UTF_8)).contentType(ContentType.TEXT_HTML);
        }

        return Response.redirect(FAILED_TEMPLATE);
    }
}
