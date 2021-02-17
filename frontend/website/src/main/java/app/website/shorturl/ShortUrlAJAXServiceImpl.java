package app.website.shorturl;

import app.api.ShortUrlAJAXService;
import app.api.UrlWebService;
import app.api.shorturl.EncodeUrlAJAXRequest;
import app.api.shorturl.EncodeUrlAJAXResponse;
import app.api.url.EncodeUrlRequest;
import app.api.url.EncodeUrlResponse;
import core.framework.inject.Inject;
import core.framework.web.rate.LimitRate;

public class ShortUrlAJAXServiceImpl implements ShortUrlAJAXService {
    @Inject
    UrlWebService urlWebService;

    @Override
    @LimitRate("encode")
    public EncodeUrlAJAXResponse encode(EncodeUrlAJAXRequest request) {
        var encodeUrlRequest = new EncodeUrlRequest();
        encodeUrlRequest.url = request.url;

        return encodeAjaxResponse(urlWebService.encode(encodeUrlRequest));
    }

    private EncodeUrlAJAXResponse encodeAjaxResponse(EncodeUrlResponse encodeResponse) {
        var response = new EncodeUrlAJAXResponse();
        response.success = encodeResponse != null && encodeResponse.encodedUrl != null ? Boolean.TRUE : Boolean.FALSE;
        response.result = response.success ? encodeResponse.encodedUrl : null;
        return response;
    }
}
