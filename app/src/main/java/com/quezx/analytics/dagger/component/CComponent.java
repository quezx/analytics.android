package com.quezx.analytics.dagger.component;

import com.quezx.analytics.dagger.module.InternetModule;
import com.quezx.analytics.dagger.module.StorageModule;
import com.quezx.analytics.dagger.scope.ApplicationScope;

import dagger.Component;

@ApplicationScope
@Component(
        modules = {
                InternetModule.class, StorageModule.class
        }, dependencies = { AppComponent.class }
)
public interface CComponent {
}
