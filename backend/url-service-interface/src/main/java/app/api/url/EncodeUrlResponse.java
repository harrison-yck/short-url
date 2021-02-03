package app.api.url;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

import java.time.ZonedDateTime;

public class EncodeUrlResponse {
    @Property(name = "encodedUrl")
    public String encodedUrl;

    @Property(name = "originalUrl")
    public String originalUrl;

    @Property(name = "createdTime")
    public ZonedDateTime createdTime;

    @Property(name = "expirationTime")
    public ZonedDateTime expirationTime;
}
