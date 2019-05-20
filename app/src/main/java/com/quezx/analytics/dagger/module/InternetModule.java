package com.quezx.analytics.dagger.module;

import com.quezx.analytics.common.helper.JsonConverter;
import com.quezx.analytics.common.helper.SharedPreferencesUtility;
import com.quezx.analytics.connectivity.AnalyticsConnection;
import com.quezx.analytics.connectivity.RetroFitConnector;
import com.squareup.okhttp.Cache;

import com.quezx.analytics.App;
import com.quezx.analytics.common.constants.EndPoints;
import com.squareup.okhttp.OkHttpClient;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

@Module
public class InternetModule {
    String baseUrl;

    public InternetModule() { this.baseUrl = EndPoints.BASE_URL;  }

    @Provides
    Cache provideOkHttpCache(App application) {
        int cacheSize = 10 * 1024 * 1024; //10 MB

        return new Cache(application.getCacheDir(),cacheSize);
    }

    @Provides
    OkHttpClient provideOkHttpClient(Cache cache) {
        OkHttpClient client = new OkHttpClient();
        client.setCache(cache);
        return client;
    }

    @Provides
    Retrofit.Builder provideRetroFitBuilder (JsonConverter gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson.getGson()))
                .baseUrl(baseUrl)
                .client(okHttpClient);
    }

    @Provides
    RetroFitConnector provideRetrofitConnctor(OkHttpClient okHttpClient,
                                              Retrofit.Builder builder) {
        return new RetroFitConnector(okHttpClient,builder);
    }

    @Provides
    AnalyticsConnection provideAnalyticsConnection(RetroFitConnector connector, JsonConverter jsonConverter,
                                                   SharedPreferencesUtility sharedPreferencesUtility){
        return new AnalyticsConnection(connector, jsonConverter, sharedPreferencesUtility);
    }

}
