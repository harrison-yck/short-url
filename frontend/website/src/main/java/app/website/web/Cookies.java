package app.website.web;

import core.framework.web.CookieSpec;

import java.time.Duration;

public class Cookies {
    public static final CookieSpec URLS = new CookieSpec("urls").secure().maxAge(Duration.ofDays(365));
}
