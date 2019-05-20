package com.quezx.analytics.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.quezx.analytics.App;
import com.quezx.analytics.R;
import com.quezx.analytics.ui.BaseActivity;

public class Demo extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
    }

    @Override
    protected void setUpComponent() {
    }
}
