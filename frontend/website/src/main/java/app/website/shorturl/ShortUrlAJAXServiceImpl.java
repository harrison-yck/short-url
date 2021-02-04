package app.website.shorturl;

import app.api.ShortUrlAJAXService;
import app.api.UrlWebService;
import app.api.shorturl.EncodeUrlAJAXRequest;
import app.api.shorturl.EncodeUrlAJAXResponse;
import app.api.url.EncodeUrlRequest;
import app.api.url.EncodeUrlResponse;
import core.framework.inject.Inject;
import core.framework.web.rate.LimitRate;

import static app.api.shorturl.EncodeUrlAJAXRequest.Availability.ONE_DAY;
import static app.api.shorturl.EncodeUrlAJAXRequest.Availability.ONE_WEEK;

public class ShortUrlAJAXServiceImpl implements ShortUrlAJAXService {
    @Inject
    UrlWebService urlWebService;

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

        return encodeAjaxResponse(urlWebService.encode(encodeUrlRequest));
    }

    private EncodeUrlAJAXResponse encodeAjaxResponse(EncodeUrlResponse encodeResponse) {
        var response = new EncodeUrlAJAXResponse();
        response.success = encodeResponse != null && encodeResponse.encodedUrl != null ? Boolean.TRUE : Boolean.FALSE;
        response.result = response.success ? encodeResponse.encodedUrl : null;
        return response;
    }
}
