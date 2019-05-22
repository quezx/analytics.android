package com.quezx.analytics.ui.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.quezx.analytics.App;
import com.quezx.analytics.MainActivity;
import com.quezx.analytics.R;
import com.quezx.analytics.common.constants.IntentConstant;
import com.quezx.analytics.common.helper.SharedPreferencesUtility;
import com.quezx.analytics.common.helper.UIHelper;
import com.quezx.analytics.connectivity.AnalyticsConnection;
import com.quezx.analytics.listener.ResultCallBack;
import com.quezx.analytics.model.UserModel;


import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatEditText;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;



import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.branch.referral.Branch;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.username)
    AppCompatEditText username;
    @BindView(R.id.password)
    AppCompatEditText password;
    @BindView(R.id.progress_bar)
    View progressBar;

    @BindView(R.id.googleSignIn)
    LinearLayout googleSignIn;

   @Inject
    AnalyticsConnection analyticsConnection;
    @Inject
    SharedPreferencesUtility sharedPreferencesUtility;
    @Inject
    UIHelper uiHelper;

    private boolean forward;
    private ResultCallBack<UserModel> loginResultCallback = new ResultCallBack<UserModel>() {
        @Override
        public void onResultCallBack(UserModel user, Exception e) {
            if (e != null) {
                Snackbar.make(rootView, getString(R.string.internet_error), Snackbar.LENGTH_SHORT)
                        .show();
            } else if (user != null) {
                Branch.getInstance(getApplicationContext()).setIdentity(String.valueOf(user.id));
                loginDone();
            } else {
                Snackbar.make(rootView, getString(R.string.invalid_username_password), Snackbar.LENGTH_SHORT).show();
            }
            uiHelper.stopProgressBar();
        }
    };

    private void loginDone() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        App.getInternetComponent().inject(this);

        forward = getIntent().getBooleanExtra(IntentConstant.FORWARD, false);

        username.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    password.requestFocus();
                    return true;
                }
                return false;
            }
        });

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onClickSubmit();
                    return true;
                }
                return false;
            }
        });
    }

    @OnClick(R.id.googleSignIn)
    public void onClickGoogle(){
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }

    @OnClick(R.id.login)
    public void onClickSubmit() {
        uiHelper.hideKeyboard(this);
        final String usernameText = username.getText().toString();
        final String passwordText = password.getText().toString();
        if (usernameText.length() == 0) {
            username.requestFocus();
            return;
        }
        if (passwordText.length() == 0) {
            password.requestFocus();
            return;
        }
        uiHelper.startProgressBar(progressBar);
        analyticsConnection.loginUser(usernameText, passwordText, loginResultCallback);
    }
}
