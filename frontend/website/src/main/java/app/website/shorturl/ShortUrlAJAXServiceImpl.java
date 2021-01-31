package app.website.shorturl;

import app.api.ShortUrlAJAXService;
import app.api.UrlWebService;
import app.api.shorturl.EncodeUrlAJAXRequest;
import app.api.shorturl.EncodeUrlAJAXResponse;
import app.api.url.EncodeUrlRequest;
import app.api.url.EncodeUrlResponse;
import core.framework.inject.Inject;
import core.framework.util.Strings;
import core.framework.web.rate.LimitRate;

import java.time.DayOfWeek;
import java.time.MonthDay;

import static app.api.shorturl.EncodeUrlAJAXRequest.Availability.*;

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

    private EncodeUrlAJAXResponse encodeAjaxResponse(EncodeUrlResponse encode) {
        var response = new EncodeUrlAJAXResponse();
        response.result = encode.result;
        response.success = Strings.isBlank(encode.result) ? Boolean.TRUE : Boolean.FALSE;
        return response;
    }
}
