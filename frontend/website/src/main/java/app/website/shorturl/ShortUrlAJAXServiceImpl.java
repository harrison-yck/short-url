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

public class ShortUrlAJAXServiceImpl implements ShortUrlAJAXService {
    @Inject
    UrlWebService urlWebService;
//    @Inject
//    Cache<ResolveUrlAJAXResponse> resolveUrlAJAXResponseCache;

    @Override
    @LimitRate("encode")
    public EncodeUrlAJAXResponse encode(EncodeUrlAJAXRequest request) {
        var encodeUrlRequest = new EncodeUrlRequest();
        encodeUrlRequest.url = request.url;

        return encodeAjaxResponse(urlWebService.encode(encodeUrlRequest));
    }

    private EncodeUrlAJAXResponse encodeAjaxResponse(EncodeUrlResponse encode) {
        var response = new EncodeUrlAJAXResponse();
        response.result = encode.result;
        response.success = Strings.isBlank(encode.result) ? Boolean.TRUE : Boolean.FALSE;
        return response;
    }
}
