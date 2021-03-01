package app;

import core.framework.module.App;
import core.framework.module.SystemModule;

import java.time.ZoneId;

public class SchedulerApp extends App {
    @Override
    protected void initialize() {
        schedule().timeZone(ZoneId.of("UTC-8"));

        load(new SystemModule("sys.properties"));
        load(new JobModule());
    }
}
