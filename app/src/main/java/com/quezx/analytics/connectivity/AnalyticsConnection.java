package com.quezx.analytics.connectivity;

import com.quezx.analytics.App;
import com.quezx.analytics.common.constants.EndPoints;
import com.quezx.analytics.common.constants.ServerConstant;
import com.quezx.analytics.common.helper.JsonConverter;
import com.quezx.analytics.common.helper.SharedPreferencesUtility;
import com.quezx.analytics.listener.ResultCallBack;
import com.quezx.analytics.model.AccessToken;
import com.quezx.analytics.model.UserModel;

import org.json.JSONObject;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class AnalyticsConnection {
    private RetroFitConnector retroFitConnector;
    private SharedPreferencesUtility sharedPreferencesUtility;
    private JsonConverter jsonConverter;


    public AnalyticsConnection(RetroFitConnector retroFitConnector, JsonConverter jsonConverter,
                               SharedPreferencesUtility sharedPreferencesUtility) {
        this.retroFitConnector = retroFitConnector;
        this.sharedPreferencesUtility = sharedPreferencesUtility;
        this.jsonConverter = jsonConverter;
    }


    //API for login in user as a user of Analytics APP
    public void loginUser(final String username, final String password, final ResultCallBack<UserModel> resultCallBack) {
        AnalyticsService service  = retroFitConnector.createLoginService(AnalyticsService.class);
        Call<AccessToken> request = service.loginUser(username,password, ServerConstant.PASSWORD,
                EndPoints.CLIENT_APP_ID,EndPoints.CLIENT_SECRET);
        request.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Response<AccessToken> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    sharedPreferencesUtility.setAccessToken(response.body());
                    getUserInfo(new ResultCallBack<UserModel>() {
                        @Override
                        public void onResultCallBack(UserModel user, Exception e) {
                            resultCallBack.onResultCallBack(user, null);
                        }
                    });
                }else {
                    String message;
                    try {
                        @SuppressWarnings("ConstantConditions") JSONObject errorBody = new JSONObject(new String
                                (response.errorBody().bytes()));
                        message = errorBody.getString("error_description");
                    } catch (Exception e) {
                        e.printStackTrace();
                        message = "";
                    }
                    resultCallBack.onResultCallBack(null,null);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace(System.err);
                resultCallBack.onResultCallBack(null,new Exception());
            }
        });
    }


    public void getUserInfo(final ResultCallBack<UserModel> resultCallBack) {
        getAccessToken(new ResultCallBack<AccessToken>() {
            @Override
            public void onResultCallBack(AccessToken accessToken, Exception e) {
                if (e != null) {
                    resultCallBack.onResultCallBack(null, e);
                    return;
                }

                AnalyticsService service = retroFitConnector.createService(AnalyticsService.class, accessToken);
                Call<UserModel> request = service.getUserInfo();
                request.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Response<UserModel> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            UserModel user = response.body();
                            sharedPreferencesUtility.setCurrentUser(user);
                            resultCallBack.onResultCallBack(user, null);
                        } else {
                            resultCallBack.onResultCallBack(null, new Exception());
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        t.printStackTrace(System.err);
                        resultCallBack.onResultCallBack(null, new Exception());
                    }
                });
            }
        });
    }

    public void getAccessToken(final ResultCallBack<AccessToken> resultCallBack) {
        AccessToken accessToken = sharedPreferencesUtility.getAccessToken();
        if (accessToken.isValid()) {
            resultCallBack.onResultCallBack(accessToken, null);
            return;
        }
        refreshToken(accessToken, resultCallBack);
    }


    //API to get the refreshToken if it is expired

    public void refreshToken(AccessToken accessToken,final ResultCallBack<AccessToken> resultCallBack) {
        AnalyticsService service = retroFitConnector.createLoginService(AnalyticsService.class);
        Call<AccessToken> accessTokenCall = service.refreshToken(ServerConstant.REFRESH_TOKEN, accessToken.refresh_token);
        accessTokenCall.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Response<AccessToken> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    sharedPreferencesUtility.setAccessToken(response.body());
                    resultCallBack.onResultCallBack(response.body(),null);
                }else {
                    resultCallBack.onResultCallBack(null, new Exception());
                    App.logout();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
