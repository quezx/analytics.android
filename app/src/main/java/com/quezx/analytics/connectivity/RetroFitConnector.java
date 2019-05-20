package com.quezx.analytics.connectivity;

import android.util.Base64;

import com.quezx.analytics.common.constants.EndPoints;
import com.quezx.analytics.common.constants.ServerConstant;
import com.quezx.analytics.model.AccessToken;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import retrofit.Retrofit;
import timber.log.Timber;

public class RetroFitConnector {

    OkHttpClient httpClient;
    Retrofit.Builder builder;

    public RetroFitConnector (OkHttpClient httpClient,Retrofit.Builder builder){
        this.httpClient = httpClient;
        this.builder = builder;
    }

    public <S> S createService (Class <S> serviceClass, AccessToken accessToken) {
        final String basic = "Bearer "+ accessToken.access_token;;
        httpClient.interceptors().clear();
        httpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder().header(ServerConstant.AUTHORIZATION, basic)
                        .method(original.method(),original.body());

                Request request = requestBuilder.build();
                Timber.e(basic);
                Timber.e(original.urlString());
                return chain.proceed(request);
            }
        });

        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }

    public <S> S createServiceNoAuthentication(Class <S> serviceClass) {
        httpClient.interceptors().clear();
        httpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();

                return chain.proceed(request);
            }
        });

        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);

    }

    public <S> S createLoginService (Class<S> serviceClass) {
        final String basic = "Bearer "+ Base64.encodeToString((EndPoints.CLIENT_APP_ID +":"+
                EndPoints.CLIENT_SECRET).getBytes(), Base64.NO_WRAP);

        httpClient.interceptors().clear();
        httpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .header(ServerConstant.AUTHORIZATION, basic).method(original.method(), original.body());
                Request request = requestBuilder.build();
                Timber.e(basic);
                Timber.e(original.urlString());
                return chain.proceed(request);
            }
        });
        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }
}
