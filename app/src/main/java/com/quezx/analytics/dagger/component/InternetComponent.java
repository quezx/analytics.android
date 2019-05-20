package com.quezx.analytics.dagger.component;

import com.quezx.analytics.dagger.module.InternetModule;
import com.quezx.analytics.dagger.module.StorageModule;
import com.quezx.analytics.dagger.scope.ApplicationScope;

import com.quezx.analytics.fragment.DashBoardFragment;
import com.quezx.analytics.ui.user.LoginActivity;

import dagger.Component;

@ApplicationScope
@Component(
        modules = {
                InternetModule.class, StorageModule.class,
        }, dependencies = { AppComponent.class }
)

public interface InternetComponent {

    void inject(LoginActivity loginActivity);
    void inject(DashBoardFragment dashBoardFragment);

}
