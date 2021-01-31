package app.api.url;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

public class ClearUrlRecordResponse {
    @NotNull
    @Property(name = "success")
    public Boolean success;
}
