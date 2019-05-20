package com.quezx.analytics.dagger.component;

import com.quezx.analytics.common.helper.SharedPreferencesUtility;
import com.quezx.analytics.dagger.module.StorageModule;
import com.quezx.analytics.dagger.scope.ApplicationScope;
import com.quezx.analytics.ui.activity.LauncherActivity;

import dagger.Component;

@ApplicationScope
@Component(
        modules = {
                StorageModule.class,
        }, dependencies = { AppComponent.class }
)
public interface StorageComponent {
    SharedPreferencesUtility getSharedPreferencesUtility();

    void inject(LauncherActivity launcherActivity);
}
