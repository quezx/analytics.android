package com.quezx.analytics.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.quezx.analytics.App;
import com.quezx.analytics.MainActivity;
import com.quezx.analytics.R;
import com.quezx.analytics.common.constants.BranchConstant;
import com.quezx.analytics.common.constants.IntentConstant;
import com.quezx.analytics.common.helper.BranchHelper;
import com.quezx.analytics.common.helper.SharedPreferencesUtility;
import com.quezx.analytics.ui.user.LoginActivity;

import java.util.HashMap;

import javax.inject.Inject;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

public class LauncherActivity extends AppCompatActivity {
    // Splash screen timer
    private static final int SPLASH_TIME_OUT = 3000;
    int checkCount = 2;
    @Inject
    SharedPreferencesUtility sharedPreferencesUtility;
    @Inject
    BranchHelper branchHelper;
    private String data;
    private String activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        App.getStorageComponent().inject(this);

        startTimer();

        checkBranchIntent();
    }

    private void startTimer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                launchActivity();
            }
        }, SPLASH_TIME_OUT);
    }

    private void checkBranchIntent() {
        Branch branch = Branch.getInstance(getApplicationContext());
        branch.initSession(new Branch.BranchUniversalReferralInitListener() {
            @Override
            public void onInitFinished(BranchUniversalObject branchUniversalObject,
                                       LinkProperties linkProperties, BranchError branchError) {
                //If not Launched by clicking Branch link
                if (branchUniversalObject == null) {
                    launchActivity();
                    return;
                }
                if (!branchUniversalObject.getMetadata().containsKey("$android_deeplink_path")) {
                    HashMap<String, String> params = branchUniversalObject.getMetadata();
                    if (params.get(BranchConstant.TYPE).equalsIgnoreCase(BranchConstant.TYPE_OPEN_ACTIVITY)) {
                        if (sharedPreferencesUtility.isUserLoggedIn() && branchHelper.openActivity(
                                LauncherActivity.this, params.get(BranchConstant.DATA),
                                params.get(BranchConstant.ACTIVITY))) {
                            finish();
                            return;
                        }
                        data = params.get(BranchConstant.DATA);
                        activity = params.get(BranchConstant.ACTIVITY);
                        launchActivity();
                    }
                }
            }
        }, this.getIntent().getData(), this);
    }

    private void launchActivity() {
        if (--checkCount > 0)
            return;
        Intent i;
        if (sharedPreferencesUtility.isUserLoggedIn()) {
                i = new Intent(this, MainActivity.class);
        } else {
            i = new Intent(this, LoginActivity.class);
        }
        if (data != null) {
            i.putExtra(IntentConstant.FORWARD, true);
            i.putExtra(IntentConstant.TYPE, IntentConstant.TYPE_BRANCH);
            i.putExtra(IntentConstant.DATA, data);
            i.putExtra(IntentConstant.ACTIVITY, activity);
        }
        startActivity(i);
        finish();
    }

}
