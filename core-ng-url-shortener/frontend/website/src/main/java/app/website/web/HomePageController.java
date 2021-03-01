package app.website.web;

import core.framework.http.ContentType;
import core.framework.util.Files;
import core.framework.web.Request;
import core.framework.web.Response;
import core.framework.web.site.WebDirectory;

import java.nio.file.Path;



public class HomePageController {
    private final Path homePage;

    public HomePageController(WebDirectory webDirectory) {
        homePage = webDirectory.path("/template/index.html");
    }

    public Response index(Request request) {
        return Response.bytes(Files.bytes(homePage)).contentType(ContentType.TEXT_HTML);
    }
}
