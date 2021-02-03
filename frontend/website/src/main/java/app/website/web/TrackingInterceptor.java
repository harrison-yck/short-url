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

        if (request.session().get("RANDOM_ID").isEmpty()) request.session().set("RANDOM_ID", randomId());
        return response;
    }

    private String randomId() {
        return UUID.randomUUID().toString();
    }
}
