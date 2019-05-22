package com.quezx.analytics.connectivity;

import com.quezx.analytics.App;
import com.quezx.analytics.common.constants.EndPoints;
import com.quezx.analytics.common.constants.ServerConstant;
import com.quezx.analytics.common.helper.JsonConverter;
import com.quezx.analytics.common.helper.SharedPreferencesUtility;
import com.quezx.analytics.listener.ResultCallBack;
import com.quezx.analytics.model.AccessToken;
import com.quezx.analytics.model.ArrayListIds;
import com.quezx.analytics.model.MappedReport;
import com.quezx.analytics.model.Name;
import com.quezx.analytics.model.ReportAddCategory;
import com.quezx.analytics.model.ReportCategory;
import com.quezx.analytics.model.Reports;
import com.quezx.analytics.model.ReportsType;
import com.quezx.analytics.model.UserDashBoard;
import com.quezx.analytics.model.UserModel;
import com.quezx.analytics.model.innerModel.UserDashBoardData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import timber.log.Timber;

public class AnalyticsConnection {
    private RetroFitConnector retroFitConnector;
    private SharedPreferencesUtility sharedPreferencesUtility;
    private JsonConverter jsonConverter;


    private final RetroFitConnector analyticsConnector;
    private final RetroFitConnector accountsConnector;



    public AnalyticsConnection(App app, List<RetroFitConnector> retroFitAPIConnector, JsonConverter jsonConverter,
                               SharedPreferencesUtility sharedPreferencesUtility) {
        retroFitConnector = retroFitAPIConnector.get(0);
        analyticsConnector = retroFitAPIConnector.get(1);
        accountsConnector = retroFitAPIConnector.get(2);
       // this.retroFitConnector = retroFitConnector;
        this.sharedPreferencesUtility = sharedPreferencesUtility;
        this.jsonConverter = jsonConverter;

        for (RetroFitConnector connector : retroFitAPIConnector) connector.initialize(this);
    }


    //API for login in user as a user of Analytics APP
    public void loginUser(final String username, final String password, final ResultCallBack<UserModel> resultCallBack) {
        AnalyticsService service  = accountsConnector.createLoginService(AnalyticsService.class);
        Call<AccessToken> request = service.loginUser(username,password, ServerConstant.PASSWORD,
                EndPoints.CLIENT_APP_ID,EndPoints.CLIENT_SECRET);
        request.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                if (response.isSuccessful()) {
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
            public void onFailure(Call<AccessToken> call, Throwable t) {
                t.printStackTrace(System.err);
                resultCallBack.onResultCallBack(null,new Exception());
            }
        });
    }


    public void getUserInfo(final ResultCallBack<UserModel> resultCallBack) {

                AnalyticsService service = accountsConnector.createService(AnalyticsService.class);
                Call<UserModel> request = service.getUserInfo();
                request.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful()) {
                            UserModel user = response.body();
                            sharedPreferencesUtility.setCurrentUser(user);
                            resultCallBack.onResultCallBack(user, null);
                        } else {
                            resultCallBack.onResultCallBack(null, new Exception());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        t.printStackTrace(System.err);
                        resultCallBack.onResultCallBack(null, new Exception());
                    }
                });
            }

   /* public void getAccessToken(final ResultCallBack<AccessToken> resultCallBack) {
        AccessToken accessToken = sharedPreferencesUtility.getAccessToken();
        if (accessToken.isValid()) {
            resultCallBack.onResultCallBack(accessToken, null);
            return;
        }
        refreshToken(accessToken, resultCallBack);
    }*/




    //API to get the database URL

    public void getDashboardURL(final int id,
                                final int dashboard_id,
                                final String metabase_url,
                                final ResultCallBack<String> callBack) {

                AnalyticsService service = analyticsConnector.createService(AnalyticsService.class);
                final Call<String> strCallBack = service.getDashboardURL(id,dashboard_id,metabase_url);
               strCallBack.enqueue(new Callback<String>() {
                   @Override
                   public void onResponse(Call<String> call, Response<String> response) {
                       if (response.isSuccessful()) {
                           callBack.onResultCallBack(response.body(), null);

                       } else {
                           try {
                               JSONObject jObjError = new JSONObject(response.errorBody().string());
                               jObjError.getString("message");

                               // Toast.makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                           } catch (Exception e) {
                               //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                           }
                       }
                   }

                   @Override
                   public void onFailure(Call<String> call, Throwable t) {
                       callBack.onResultCallBack(null, new Exception());

                   }
               });

            }

    //API to get the refreshToken if it is expired
    public int refreshAccessToken(AccessToken accessToken) {
        AnalyticsService service = retroFitConnector.createLoginService(AnalyticsService.class);
        Call<AccessToken> request = service.refreshToken(ServerConstant.REFRESH_TOKEN, accessToken.refresh_token);
        try {
            Response<AccessToken> response = request.execute();
            if (response.isSuccessful()) {
                sharedPreferencesUtility.setAccessToken(response.body());
            } else { // TODO check if no internet
                App.logout();
            }
            return response.code();
        } catch (Exception e) {
            Timber.w(e);
        }
        return 0;
    }

    public void getReportList(final ResultCallBack<Reports> callBack) {
        AnalyticsService service = analyticsConnector.createService(AnalyticsService.class);
        final Call<Reports>request = service.getReportsList();
        request.enqueue(new Callback<Reports>() {
            @Override
            public void onResponse(Call<Reports> call, Response<Reports> response) {
                if (response.isSuccessful()) {
                    callBack.onResultCallBack(response.body(),null);
                }else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        jObjError.getString("message");

                        // Toast.makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Reports> call, Throwable t) {
                callBack.onResultCallBack(null, new Exception());
            }
        });
    }

    public void getUserDashBoard(final ResultCallBack<UserDashBoard> callBack){
        AnalyticsService service = analyticsConnector.createService(AnalyticsService.class);
        final Call<UserDashBoard> request = service.getUserDashboard();
        request.enqueue(new Callback<UserDashBoard>() {
            @Override
            public void onResponse(Call<UserDashBoard> call, Response<UserDashBoard> response) {
                if (response.isSuccessful()) {
                    callBack.onResultCallBack(response.body(),null);
                }else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        jObjError.getString("message");

                        // Toast.makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserDashBoard> call, Throwable t) {
                callBack.onResultCallBack(null, new Exception());
            }
        });
    }

    public void getReportsCategory(final int id, final ResultCallBack<ReportsType> callBack) {
        AnalyticsService service = analyticsConnector.createService(AnalyticsService.class);
        final Call<ReportsType> request = service.getReportsCategory(id);
        request.enqueue(new Callback<ReportsType>() {
            @Override
            public void onResponse(Call<ReportsType> call, Response<ReportsType> response) {
                if (response.isSuccessful()){
                    callBack.onResultCallBack(response.body(),null);
                }else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        jObjError.getString("message");

                        // Toast.makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ReportsType> call, Throwable t) {
                callBack.onResultCallBack(null, new Exception());
            }
        });

    }

    public void postReportCategory(final int id, final ArrayListIds integerArrayList, final ResultCallBack<ReportCategory> callBack) {
        AnalyticsService service = analyticsConnector.createService(AnalyticsService.class);
        final Call<ReportCategory> request = service.postReportCategory(id,integerArrayList);
        request.enqueue(new Callback<ReportCategory>() {
            @Override
            public void onResponse(Call<ReportCategory> call, Response<ReportCategory> response) {

                if (response.isSuccessful()) {
                    callBack.onResultCallBack(response.body(),null);
                }else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        jObjError.getString("message");

                        // Toast.makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ReportCategory> call, Throwable t) {
                callBack.onResultCallBack(null, new Exception());
            }
        });
    }

    public void createReportCategory(Name name, final ResultCallBack<ReportAddCategory> callBack) {
        AnalyticsService service =  analyticsConnector.createService(AnalyticsService.class);
        final Call<ReportAddCategory> request =service.createReportCategory(name);

        request.enqueue(new Callback<ReportAddCategory>() {
            @Override
            public void onResponse(Call<ReportAddCategory> call, Response<ReportAddCategory> response) {
                if (response.isSuccessful()) {
                    callBack.onResultCallBack(response.body(),null);
                }else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        jObjError.getString("message");

                        // Toast.makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ReportAddCategory> call, Throwable t) {
                callBack.onResultCallBack(null, new Exception());
            }
        });
    }

    public void getMappedReport(final int report_id, final ResultCallBack<MappedReport> callBack) {
        AnalyticsService service = analyticsConnector.createService(AnalyticsService.class);
        final Call<MappedReport> request = service.getMappedReport(report_id);
        request.enqueue(new Callback<MappedReport>() {
            @Override
            public void onResponse(Call<MappedReport> call, Response<MappedReport> response) {
                if (response.isSuccessful()) {
                    callBack.onResultCallBack(response.body(),null);
                }else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        jObjError.getString("message");

                        // Toast.makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<MappedReport> call, Throwable t) {
                callBack.onResultCallBack(null, new Exception());
            }
        });
    }

    public void getReportMetabaseUrl(final int report_id, final int question_id, final ResultCallBack<String> callBack) {
        AnalyticsService service = analyticsConnector.createService(AnalyticsService.class);
        final Call<String> request = service.getReportsMetabaseUrl(report_id,ServerConstant.METABASE_URL,question_id);
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    callBack.onResultCallBack(response.body(),null);
                }else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        jObjError.getString("message");

                        // Toast.makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callBack.onResultCallBack(null, new Exception());
            }
        });
    }

    public void setAsDefaultDashBoard(final int id, final ResultCallBack<ResponseBody> callBack) {
        AnalyticsService service = analyticsConnector.createService(AnalyticsService.class);
        final Call<ResponseBody> request = service.setasDefaultDashboard(id);
        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callBack.onResultCallBack(response.body(),null);
                }else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        jObjError.getString("message");

                        // Toast.makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callBack.onResultCallBack(null, new Exception());
            }
        });

    }

}
