package com.quezx.analytics.ui.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.quezx.analytics.R;
import com.quezx.analytics.connectivity.AnalyticsConnection;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareDashboard extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    AppCompatTextView toolbarTitle;

    @Inject
    AnalyticsConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_dashboard);


        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setUpMenuComponent();
    }


    private void setUpMenuComponent() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP);
            getSupportActionBar().setCustomView(R.layout.menu_report_type_list);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            View menu = (getSupportActionBar().getCustomView());
            toolbarTitle = menu.findViewById(R.id.toolbar_title);
            toolbarTitle.setText("TEST NAMWE");
            //TODO SET TITLE COMING FROM INTENT toolbarTitle.setText(name);

        }
    }
}
