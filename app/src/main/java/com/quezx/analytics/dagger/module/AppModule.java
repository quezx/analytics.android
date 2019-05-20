package com.quezx.analytics.dagger.module;

import com.quezx.analytics.App;
import com.quezx.analytics.common.helper.BranchHelper;
import com.quezx.analytics.common.helper.FileHelper;
import com.quezx.analytics.common.helper.JsonConverter;
import com.quezx.analytics.common.helper.UIHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private App app;

    public AppModule(App app) { this.app = app; }

    @Provides
    @Singleton
    public App provideApp() { return app; }

    @Provides
    @Singleton
    JsonConverter provideJsonConverter() { return new JsonConverter(); }

    @Provides
    @Singleton
    BranchHelper provideBranchHelper() {
        return new BranchHelper();
    }

    @Provides
    @Singleton
    public FileHelper provideFileHelper() {
        return new FileHelper();
    }

    @Provides
    @Singleton
    public UIHelper provideUIhelper() { return new UIHelper(); }
}
