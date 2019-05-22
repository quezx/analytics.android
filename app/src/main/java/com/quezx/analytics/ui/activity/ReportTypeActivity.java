package com.quezx.analytics.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.quezx.analytics.App;
import com.quezx.analytics.R;
import com.quezx.analytics.adapter.ReportsCategoryAdapter;
import com.quezx.analytics.adapter.ReportsTypeAdapter;
import com.quezx.analytics.common.constants.IntentConstant;
import com.quezx.analytics.common.helper.JsonConverter;
import com.quezx.analytics.common.helper.UIHelper;
import com.quezx.analytics.connectivity.AnalyticsConnection;
import com.quezx.analytics.listener.ResultCallBack;
import com.quezx.analytics.model.ReportsType;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportTypeActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    AppCompatTextView toolbarTitle;

    @Inject
    AnalyticsConnection connection;
    @Inject
    JsonConverter json;
    @Inject
    UIHelper uiHelper;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    View progressBar;
    @BindView(R.id.search_block)
    View searchBlock;
    @BindView(R.id.search)
    AppCompatEditText searchBar;

    @BindView(R.id.nodatafound)
    View nodatafound;
    @BindView(R.id.ifdatafound)
    View ifdatafound;

    private ReportsTypeAdapter reportsTypeAdapter;
    private Context context;
    int reportId;
    String reportName;

    private ResultCallBack<ReportsType> typeResultCallBack = new ResultCallBack<ReportsType>() {
        @Override
        public void onResultCallBack(ReportsType object, Exception e) {
            if (object.getData() != null) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                reportsTypeAdapter.getReports().clear();
                reportsTypeAdapter.getReports().addAll(object.getData());

                reportsTypeAdapter.notifyDataSetChanged();
            }else {
                progressBar.setVisibility(View.GONE);
                //  search_block.setVisibility(View.GONE);
                Toast.makeText(ReportTypeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_type);

        reportId = getIntent().getIntExtra(IntentConstant.REPORTID, 0);
        reportName = getIntent().getStringExtra(IntentConstant.FILTER_NAME);

        ButterKnife.bind(this);
        App.getInternetComponent().inject(this);
        setSupportActionBar(toolbar);
        setUpMenuComponent();
        getReportsCategory(reportId);



        reportsTypeAdapter = new ReportsTypeAdapter(ReportTypeActivity.this,uiHelper);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(reportsTypeAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.report_type_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent i;
        switch ( id ) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.menu_add_report_type:
                Intent intent = new Intent(ReportTypeActivity.this,AddReports.class);
                intent.putExtra(IntentConstant.REPORTID,reportId);
                intent.putExtra(IntentConstant.FILTER_NAME,reportName);
                startActivityForResult(intent,IntentConstant.ACTIVITYRESULTDATA);

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
            toolbarTitle.setText(reportName);
         //TODO SET TITLE COMING FROM INTENT toolbarTitle.setText(name);

        }
    }

    public void getReportsCategory(int id) {
        connection.getReportsCategory(id,typeResultCallBack);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntentConstant.ACTIVITYRESULTDATA) {
            getReportsCategory(reportId);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
