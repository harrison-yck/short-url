package app.website.shorturl;

import app.api.ShortUrlAJAXService;
import app.api.UrlWebService;
import app.api.shorturl.EncodeUrlAJAXRequest;
import app.api.shorturl.EncodeUrlAJAXResponse;
import app.api.url.EncodeUrlRequest;
import app.api.url.EncodeUrlResponse;
import app.website.web.Cookies;
import core.framework.inject.Inject;
import core.framework.web.WebContext;
import core.framework.web.exception.BadRequestException;
import core.framework.web.rate.LimitRate;

import static app.api.shorturl.EncodeUrlAJAXRequest.Availability.ONE_DAY;
import static app.api.shorturl.EncodeUrlAJAXRequest.Availability.ONE_WEEK;

public class ShortUrlAJAXServiceImpl implements ShortUrlAJAXService {
    @Inject
    UrlWebService urlWebService;
    @Inject
    WebContext webContext;

    @Override
    @LimitRate("encode")
    public EncodeUrlAJAXResponse encode(EncodeUrlAJAXRequest request) {
        var encodeUrlRequest = new EncodeUrlRequest();
        encodeUrlRequest.url = request.url;

        if (request.availability == ONE_DAY) {
            encodeUrlRequest.lastForDays = 1;
        } else if (request.availability == ONE_WEEK) {
            encodeUrlRequest.lastForDays = 7;
        } else {
            throw new Error("unsupported operation, empty lastForDays");
        }
        EncodeUrlResponse response = urlWebService.encode(encodeUrlRequest);

//        webContext.request().cookie(Cookies.URLS).;

        return encodeAjaxResponse(response);
    }

    private EncodeUrlAJAXResponse encodeAjaxResponse(EncodeUrlResponse encodeResponse) {
        var response = new EncodeUrlAJAXResponse();
        response.success = encodeResponse.encodedUrl != null ? Boolean.TRUE : Boolean.FALSE;
        response.result = response.success ? encodeResponse.encodedUrl : null;
        return response;
    }
}
