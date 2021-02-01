package app.website.web;

import core.framework.web.CookieSpec;

import java.time.Duration;

public class Cookies {
    public static final CookieSpec RANDOM_ID = new CookieSpec("random_id").secure().maxAge(Duration.ofDays(365));
}
