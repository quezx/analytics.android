package com.quezx.analytics.connectivity.interceptor;



import com.quezx.analytics.App;
import com.quezx.analytics.BuildConfig;
import com.quezx.analytics.common.constants.ServerConstant;
import com.quezx.analytics.common.helper.SharedPreferencesUtility;
import com.quezx.analytics.connectivity.AnalyticsConnection;
import com.quezx.analytics.model.AccessToken;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class AuthorizationInterceptor implements Interceptor {

	private final SharedPreferencesUtility spUtility;
	private final AnalyticsConnection connection;

	public AuthorizationInterceptor(SharedPreferencesUtility spUtility, AnalyticsConnection connection) {
		this.spUtility = spUtility;
		this.connection = connection;
	}

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();
		AccessToken token = spUtility.getAccessToken();
		//Build new request
		Request.Builder builder = request.newBuilder();
		request = builder.build();
		String basic = "no-token-found";
		if (token != null) {
			basic = String.format("Bearer %s", token.access_token);
			builder = builder.header(ServerConstant.AUTHORIZATION, basic);
			builder = builder.method(request.method(), request.body());
			request = builder.build();
		}
		if (BuildConfig.DEBUG) {
			Timber.d(basic);
			Timber.d(request.method() + " : " + request.url().toString());
		}
		Response response = chain.proceed(request);
		if (response.code() != 200)
			Timber.d(request.method() + " : " + request.url().toString() + ":" + response.code());
		if (response.code() == 401) { //if unauthorized
			String requestToken = request.headers().get(ServerConstant.AUTHORIZATION).split(" ")[1];
			synchronized (connection) { //perform all 401 in sync blocks, to avoid multiply token updates
				AccessToken accessToken = spUtility.getAccessToken(); //get currently stored token
				if (accessToken != null && accessToken.access_token.equals(requestToken)) { //compare
					// current token with token
					// that was stored before, if it was not updated - do update
					int code = connection.refreshAccessToken(accessToken);
					if (code != 200) {
						App.logout();
						return response;
					}
					accessToken = spUtility.getAccessToken();
				}
				if (accessToken == null) return response;
				//retry with new auth token,
				builder.header(ServerConstant.AUTHORIZATION, String.format("Bearer %s", accessToken
								.access_token)).method(request.method(), request.body());

				Request newRequest = builder.build();
				return chain.proceed(newRequest); //repeat request with new token
			}
		}

		return response;
	}
}
