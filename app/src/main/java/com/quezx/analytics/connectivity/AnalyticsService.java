package com.quezx.analytics.connectivity;

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
import com.quezx.analytics.model.innerModel.ReportData;
import com.quezx.analytics.model.innerModel.UserDashBoardData;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AnalyticsService {
    @FormUrlEncoded
    @POST("/oauth/token")
    Call<AccessToken> loginUser(@Field("username") String username,
                                @Field("password") String password,
                                @Field("grant_type") String grant_type,
                                @Field("client_id") String client_id,
                                @Field("client_secret") String client_secret);

    @FormUrlEncoded
    @POST("/oauth/token")
    Call<AccessToken> refreshToken(@Field("grant_type") String grant_type,
                                   @Field("refresh_token") String refresh_token);

    @GET("/api/users/me")
    Call<UserModel> getUserInfo();

    @GET("/api/dashboard/{ID}")
    Call<String> getDashboardURL(@Path("ID") int id,
                                 @Query("dashboard_id") int dashboard_id,
                                 @Query("metabaseUrl") String metabaseUrl );
    @GET("/api/category")
    Call<Reports> getReportsList();

    @GET("/api/reportCategory/{ID}")
    Call<ReportsType> getReportsCategory(@Path("ID") int id);

    @GET("/api/userDashboard")
    Call<UserDashBoard> getUserDashboard();

    @PUT("/api/userDashboard/{ID}/setDefault")
    Call<ResponseBody> setasDefaultDashboard(@Path("ID") int id);

    @GET("/api/report/{report_id}")
    Call<String> getReportsMetabaseUrl(@Path("report_id") int report_id,
                                       @Query("metabaseUrl") String metabaseUrl,
                                       @Query("question_id") int question_id);

    @GET("/api/reportCategory/{report_id}/upMappedReport")
    Call<MappedReport> getMappedReport(@Path("report_id") int report_id);

    @POST("/api/reportCategory/{ID}")
    Call<ReportCategory> postReportCategory(@Path("ID") int id,@Body ArrayListIds ages);

    @POST("/api/category")
    Call<ReportAddCategory> createReportCategory(@Body Name name);

}
