package app.website.web;

import core.framework.http.ContentType;
import core.framework.util.Files;
import core.framework.web.Request;
import core.framework.web.Response;
import core.framework.web.site.WebDirectory;

import java.nio.file.Path;



public class HomePageController {
    private final Path HOMEPAGE;

    public HomePageController(WebDirectory webDirectory) {
        HOMEPAGE = webDirectory.path("/template/index.html");
    }

    public Response index(Request request) {
        System.out.println("index page");
        return Response.bytes(Files.bytes(HOMEPAGE)).contentType(ContentType.TEXT_HTML);
    }
}
