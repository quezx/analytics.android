package com.quezx.analytics;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.quezx.analytics.common.constants.EndPoints;
import com.quezx.analytics.common.helper.SharedPreferencesUtility;
import com.quezx.analytics.dagger.component.AppComponent;
import com.quezx.analytics.dagger.component.DaggerAppComponent;
//import com.quezx.analytics.dagger.component.DaggerInternetComponent;
import com.quezx.analytics.dagger.component.DaggerInternetComponent;
import com.quezx.analytics.dagger.component.DaggerStorageComponent;
import com.quezx.analytics.dagger.component.InternetComponent;
import com.quezx.analytics.dagger.component.StorageComponent;
import com.quezx.analytics.dagger.module.AppModule;
import com.quezx.analytics.model.UserModel;
import com.quezx.analytics.ui.user.LoginActivity;

import io.branch.referral.Branch;
import timber.log.Timber;

public class App extends Application {

    private static Context appContext;
    private static InternetComponent internetComponent;
    private static StorageComponent storageComponent;

    public static InternetComponent getInternetComponent() { return internetComponent; }

    public static StorageComponent getStorageComponent() { return storageComponent; }



    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();

        //Initialise EndPoints
        EndPoints.initialize(appContext);

        //Branch.io
        Branch.getAutoInstance(this);

        //setting up dagger
        setUpDagger();

        //Initialise Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
           //TODO put Fabric.with(this, new Crashlytics());
            //TODO after Adding Crashalytics Timber.plant(new CrashlyticsTree());
        }
    }

    private void setUpDagger() {

        AppModule appModule = new AppModule(this);

        AppComponent appComponent = DaggerAppComponent.builder()
                .appModule(appModule)
                .build();

        appComponent.inject(this);

        storageComponent = DaggerStorageComponent.builder()
                .appComponent(appComponent)
                .build();

        internetComponent = DaggerInternetComponent.builder()
                .appComponent(appComponent)
                .build();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static void logout() {
        SharedPreferencesUtility sharedPreferencesUtility = new SharedPreferencesUtility(appContext);
        UserModel userModel = sharedPreferencesUtility.getCurrentUser();
        sharedPreferencesUtility.reset();
        goToLoginScreen();
    }

    public static void goToLoginScreen() {
        Intent intent = new Intent(appContext, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        appContext.startActivity(intent);
    }
}
