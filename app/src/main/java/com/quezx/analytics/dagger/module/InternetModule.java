package com.quezx.analytics.dagger.module;

import com.quezx.analytics.common.helper.JsonConverter;
import com.quezx.analytics.common.helper.SharedPreferencesUtility;
import com.quezx.analytics.connectivity.AnalyticsConnection;
import com.quezx.analytics.connectivity.RetroFitConnector;
import okhttp3.Cache;

import com.quezx.analytics.App;
import com.quezx.analytics.common.constants.EndPoints;


import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class InternetModule {
    String baseUrl;
    private final String qanalyticsURL;
    private final String accountsURL;

    public InternetModule() {
        this.qanalyticsURL = EndPoints.ANALYTICSURL;
        this.baseUrl = EndPoints.BASE_URL;
        this.accountsURL = EndPoints.ACCOUNTSURL;
    }

    @Provides
    Cache provideOkHttpCache(App application) {
        int cacheSize = 10 * 1024 * 1024; //10 MB

        return new Cache(application.getCacheDir(),cacheSize);
    }


    @Provides
    List<Retrofit.Builder> provideRetroFitBuilder (JsonConverter gson) {
        List<Retrofit.Builder> list = new ArrayList<>();
        list.add(new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson.getGson())).baseUrl(baseUrl));
        list.add(new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson.getGson())).baseUrl(qanalyticsURL));
        list.add(new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson.getGson())).baseUrl(accountsURL));
        return list;
    }

    @Provides
    List<RetroFitConnector> provideRetrofitConnector(Cache cache, List<retrofit2.Retrofit.Builder> builders, SharedPreferencesUtility spUtility) {
        List<RetroFitConnector> list = new ArrayList<>();
        for (retrofit2.Retrofit.Builder builder : builders) {
            list.add(new RetroFitConnector(cache, builder, spUtility));
        }
        return list;
    }


    @Provides
    AnalyticsConnection provideAnalyticsConnection(App app, List<RetroFitConnector> connectors, JsonConverter gson, SharedPreferencesUtility
            SPUtility) {
        return new AnalyticsConnection(app, connectors, gson, SPUtility);
    }

}
