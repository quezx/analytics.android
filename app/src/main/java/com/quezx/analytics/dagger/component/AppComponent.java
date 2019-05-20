package com.quezx.analytics.dagger.component;

import com.quezx.analytics.App;
import com.quezx.analytics.common.helper.BranchHelper;
import com.quezx.analytics.common.helper.JsonConverter;
import com.quezx.analytics.common.helper.FileHelper;
import com.quezx.analytics.common.helper.UIHelper;
import com.quezx.analytics.dagger.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                AppModule.class
        }
)
public interface AppComponent {


    App getApp();

    UIHelper getUiHelper();

    FileHelper getFileHelper();

    JsonConverter getJsonConverter();

    BranchHelper getBranchHelper();

    void inject(App app);
}
