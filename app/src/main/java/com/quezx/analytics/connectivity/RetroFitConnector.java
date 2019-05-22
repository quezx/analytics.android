package com.quezx.analytics.connectivity;



import com.quezx.analytics.common.helper.SharedPreferencesUtility;
import com.quezx.analytics.connectivity.interceptor.AuthorizationInterceptor;
import com.quezx.analytics.connectivity.interceptor.LoginInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

public class RetroFitConnector {

    private final SharedPreferencesUtility spUtility;
    private final Cache cache;
    private final Retrofit.Builder builder;
    private Retrofit authentication;
    private Retrofit login;

    public RetroFitConnector(Cache cache, Retrofit.Builder builder, SharedPreferencesUtility spUtility) {
        this.cache = cache;
        this.builder = builder;
        this.spUtility = spUtility;
    }

    public void initialize(AnalyticsConnection connection) {
        authentication = builder.client(getOkHttpBuilder().addInterceptor(new AuthorizationInterceptor(spUtility,
                connection)).build()).build();
        login = builder.client(getOkHttpBuilder().addInterceptor(new LoginInterceptor()).build()).build();
    }

    <S> S createService(Class<S> serviceClass) {
        return authentication.create(serviceClass);
    }

    public <S> S createServiceNoAuthentication(Class<S> serviceClass) {
        OkHttpClient httpClient = getOkHttpBuilder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder().method(original.method(), original.body());

                Request request = requestBuilder.build();
                Timber.d(original.url().toString());
                return chain.proceed(request);
            }
        }).build();
        Retrofit retrofit = this.builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }

    private OkHttpClient.Builder getOkHttpBuilder() {
        return new OkHttpClient.Builder().cache(cache).connectTimeout(10, TimeUnit.SECONDS).readTimeout(30, TimeUnit
                .SECONDS);
    }

    <S> S createLoginService(Class<S> serviceClass) {
        return login.create(serviceClass);
    }
}
