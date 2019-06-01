package com.quezx.analytics.ui.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.quezx.analytics.App;
import com.quezx.analytics.R;
import com.quezx.analytics.common.helper.SharedPreferencesUtility;
import com.quezx.analytics.common.helper.UIHelper;
import com.quezx.analytics.connectivity.AnalyticsConnection;
import com.quezx.analytics.listener.ResultCallBack;
import com.quezx.analytics.model.Name;
import com.quezx.analytics.model.ReportAddCategory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddCategory extends AppCompatActivity {

    @BindView(R.id.categoryName)
    AppCompatEditText categoryName;

    @BindView(R.id.cancel)
    Button cancel;

    @BindView(R.id.save)
    Button save;

    @Inject
    AnalyticsConnection analyticsConnection;
    @Inject
    SharedPreferencesUtility sharedPreferencesUtility;
    @Inject
    UIHelper uiHelper;

    @BindView(R.id.progress_bar)
    View progressBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    AppCompatTextView toolbarTitle;

    final ResultCallBack<ReportAddCategory> reportAddCategoryResultCallBack = new ResultCallBack<ReportAddCategory>() {
        @Override
        public void onResultCallBack(ReportAddCategory object, Exception e) {
            uiHelper.stopProgressBar();
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        ButterKnife.bind(this);

        App.getInternetComponent().inject(this);
        setSupportActionBar(toolbar);
        setUpMenuComponent();



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpMenuComponent() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP);
            getSupportActionBar().setCustomView(R.layout.menu_report_type_list);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            View menu = (getSupportActionBar().getCustomView());
            toolbarTitle = menu.findViewById(R.id.toolbar_title);
            toolbarTitle.setText("Add Category");
        }
    }


    @OnClick(R.id.cancel)
    public void setCancel() {
        finish();
    }

    @OnClick(R.id.save)
    public void setSave() {

        uiHelper.hideKeyboard(this);
        final String categoryNameText = categoryName.getText().toString();

        if (categoryNameText.length() == 0) {
            categoryName.requestFocus();
            return;
        }

        uiHelper.startProgressBar(progressBar);
        analyticsConnection.createReportCategory(new Name(categoryNameText),reportAddCategoryResultCallBack);

    }
}
