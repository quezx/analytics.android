package com.quezx.analytics.common.constants;

import android.content.Context;

import com.quezx.analytics.R;

public class EndPoints {

    public static String BASE_URL ;
    public static String WEB_BASE_URL;
    public static String WEB_APPLICANT_URL ;

   /* public static final String CLIENT_APP_ID = "analyticsquezx";
    public static final String CLIENT_SECRET = "analyticssecret";
*/

    public static final String CLIENT_APP_ID = "quezxanalytics";
    public static final String CLIENT_SECRET = "quezxanalyticssecret";

    public static void initialize(Context context){
        BASE_URL=context.getString(R.string.BASE_URL_API);
        WEB_BASE_URL=context.getString(R.string.WEB_BASE_URL);
        WEB_APPLICANT_URL = WEB_BASE_URL + "/applicants/%s";
    }

}
