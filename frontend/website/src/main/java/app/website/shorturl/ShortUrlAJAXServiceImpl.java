package app.website.shorturl;

import app.api.ShortUrlAJAXService;
import app.api.UrlWebService;
import app.api.shorturl.EncodeUrlAJAXRequest;
import app.api.shorturl.EncodeUrlAJAXResponse;
import app.api.url.EncodeUrlRequest;
import app.api.url.EncodeUrlResponse;
import app.website.web.Cookies;
import core.framework.inject.Inject;
import core.framework.util.Strings;
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
        encodeUrlRequest.randomStr = webContext.request().session().get(Cookies.RANDOM_ID.name).orElseThrow(() -> new BadRequestException("Please enable cookie"));

        if (request.availability == ONE_DAY) {
            encodeUrlRequest.lastForDays = 1;
        } else if (request.availability == ONE_WEEK) {
            encodeUrlRequest.lastForDays = 7;
        } else {
            throw new Error("unsupported operation, empty lastForDays");
        }

        return encodeAjaxResponse(urlWebService.encode(encodeUrlRequest));
    }

    private EncodeUrlAJAXResponse encodeAjaxResponse(EncodeUrlResponse encode) {
        var response = new EncodeUrlAJAXResponse();
        response.result = encode.result;
        response.success = Strings.isBlank(encode.result) ? Boolean.TRUE : Boolean.FALSE;
        return response;
    }
}
