package com.quezx.analytics.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.quezx.analytics.App;
import com.quezx.analytics.R;
import com.quezx.analytics.common.helper.BranchHelper;
import com.quezx.analytics.common.helper.SharedPreferencesUtility;
import com.quezx.analytics.connectivity.AnalyticsConnection;
import com.quezx.analytics.ui.BaseActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class SetUpActivity extends BaseActivity {

    private static final int RETRY_DELAY = 2 * 1000;
    @Inject
    AnalyticsConnection analyticsConnection;
    @Inject
    SharedPreferencesUtility sharedPreferencesUtility;
    @Inject
    BranchHelper branchHelper;

    private boolean forward;
    private int maxStateTry = 5;
    private int maxReasonTry = 5;
    private int pendingRequest = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);

        ButterKnife.bind(this);
    }

    @Override
    protected void setUpComponent() {



    }


}
