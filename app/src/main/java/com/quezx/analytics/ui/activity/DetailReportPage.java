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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.quezx.analytics.App;
import com.quezx.analytics.R;
import com.quezx.analytics.common.constants.IntentConstant;
import com.quezx.analytics.connectivity.AnalyticsConnection;
import com.quezx.analytics.fragment.DashBoardFragment;
import com.quezx.analytics.listener.ResultCallBack;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailReportPage extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.webView)
    WebView webView;

    AppCompatTextView toolbarTitle;

    @Inject
    AnalyticsConnection connection;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.progressBar_layout)
    View progressBar_layout;

    private int question_id, report_id;

    String recievedURL;


    private ResultCallBack<String> stringResultCallBack = new ResultCallBack<String>() {
        @Override
        public void onResultCallBack(String url, Exception e) {
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webView.setWebViewClient(new AppWebViewClients());
            webView.loadUrl(url);

            recievedURL = url;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_report_page);

        ButterKnife.bind(this);
        App.getInternetComponent().inject(this);
        setSupportActionBar(toolbar);
        setUpMenuComponent();
        question_id = getIntent().getIntExtra(IntentConstant.QUESTIONID,0);
        report_id = getIntent().getIntExtra(IntentConstant.REPORTID,0);

        getReportsMetabaseUrl(report_id,question_id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboardmenu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    public void getReportsMetabaseUrl(int report_id, int question_id) {
        connection.getReportMetabaseUrl(report_id,question_id,stringResultCallBack);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent i;
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.menu_share:
               shareTextUrl();
               break;
                //TODO WRITE CODE OR SHARE

        }

        return super.onOptionsItemSelected(item);
    }

    private void shareTextUrl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.title_dashboard));
        share.putExtra(Intent.EXTRA_TEXT, recievedURL);

        startActivity(Intent.createChooser(share, "Share DashBoard!"));
    }


    private void setUpMenuComponent() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP);
            getSupportActionBar().setCustomView(R.layout.menu_report_type_list);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            View menu = (getSupportActionBar().getCustomView());
            toolbarTitle = menu.findViewById(R.id.toolbar_title);
            toolbarTitle.setText("DETAIL REPORT");
            //TODO SET TITLE COMING FROM INTENT toolbarTitle.setText(name);

        }
    }

    public class AppWebViewClients extends WebViewClient {


        public AppWebViewClients() {
            progressBar_layout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            // uiHelper.startProgressBar(quezxLoading);
            //  uiHelper.startProgressBar(WebViewActivity.this,"Please Wait");
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            progressBar_layout.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            // uiHelper.stopProgressBar();
        }
    }
}
