package app;

import core.framework.test.module.AbstractTestModule;

public class TestModule extends AbstractTestModule {
    @Override
    protected void initialize() {
        load(new ShortUrlApp());
    }
}
