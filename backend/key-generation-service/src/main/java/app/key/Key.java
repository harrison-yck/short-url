package app.key;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

public class Key {
    @NotNull
    @Property(name = "id")
    public String id;

    @NotNull
    @Property(name = "value")
    public String value;
}
