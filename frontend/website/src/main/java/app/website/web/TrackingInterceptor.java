package app.website.web;

import core.framework.web.CookieSpec;
import core.framework.web.Interceptor;
import core.framework.web.Invocation;
import core.framework.web.Request;
import core.framework.web.Response;

import java.util.Optional;
import java.util.UUID;

public class TrackingInterceptor implements Interceptor {
    @Override
    public Response intercept(Invocation invocation) throws Exception {
        Request request = invocation.context().request();
        Response response = invocation.proceed();

        Optional<String> cookie = request.cookie(Cookies.RANDOM_ID);
        String randomId = cookie.orElseGet(this::createId);

        if (cookie.isEmpty()) {
            response.cookie(new CookieSpec(Cookies.RANDOM_ID.name).secure().sameSite().maxAge(Cookies.RANDOM_ID.maxAge), randomId);
        }

        if (request.session().get(Cookies.RANDOM_ID.name).isEmpty()) {
            request.session().set(Cookies.RANDOM_ID.name, randomId);
        }

        return response;
    }

    private String createId() {
        return UUID.randomUUID().toString();
    }
}
