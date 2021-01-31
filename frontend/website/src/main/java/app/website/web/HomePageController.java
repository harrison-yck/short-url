package app.website.web;

import core.framework.http.ContentType;
import core.framework.util.Files;
import core.framework.web.Request;
import core.framework.web.Response;
import core.framework.web.exception.NotFoundException;
import core.framework.web.site.WebDirectory;

import java.nio.file.Path;



public class HomePageController {
    private final Path HOMEPAGE;

    public HomePageController(WebDirectory webDirectory) {
        HOMEPAGE = webDirectory.path("/index.html");
    }

    public Response index(Request request) {
        if (request.path().startsWith("/ajax")) throw new NotFoundException("page not found");
        return Response.bytes(Files.bytes(HOMEPAGE)).contentType(ContentType.TEXT_HTML);
    }
}
