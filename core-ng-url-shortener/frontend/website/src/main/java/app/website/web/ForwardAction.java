package app.website.web;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

/**
 * @author harrison
 */
public class ForwardAction {
    @NotNull
    @Property(name = "action")
    public String action;
}
