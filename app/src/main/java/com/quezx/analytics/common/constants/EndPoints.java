package com.quezx.analytics.common.constants;

import android.content.Context;

import com.quezx.analytics.R;

public class EndPoints {

    public static String BASE_URL ;
    public static String ANALYTICSURL;
    public static String ACCOUNTSURL;

    public static final String CLIENT_APP_ID = "analyticsquezx";
    public static final String CLIENT_SECRET = "analyticssecret";

   /* public static final String CLIENT_APP_ID = "quezxanalytics";
    public static final String CLIENT_SECRET = "quezxanalyticssecret";*/

    public static void initialize(Context context){
        BASE_URL=context.getString(R.string.BASE_URL_API);
        ANALYTICSURL = context.getString(R.string.ANALYTICS_URL);
        ACCOUNTSURL  = context.getString(R.string.ACCOUNTS_URL);
    }

}
