package com.quezx.analytics.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.quezx.analytics.App;
import com.quezx.analytics.R;
import com.quezx.analytics.common.constants.IntentConstant;
import com.quezx.analytics.connectivity.AnalyticsConnection;
import com.quezx.analytics.listener.ResultCallBack;
import com.quezx.analytics.model.ArrayListIds;
import com.quezx.analytics.model.MappedReport;
import com.quezx.analytics.model.ReportCategory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddReports extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.add)
    Button add;
    AppCompatTextView toolbarTitle;

    @Inject
    AnalyticsConnection connection;

    int reportId;
    String reportName;

    ArrayList<Integer> listofSelectedIds = new ArrayList<>();


    @BindView(R.id.progressBar_layout)
    View progressBar_layout;
    private ResultCallBack<ReportCategory> reportCategoryResultCallBack = new ResultCallBack<ReportCategory>() {
        @Override
        public void onResultCallBack(ReportCategory object, Exception e) {
            Toast.makeText(AddReports.this, "SUCCESSFULLY ADDED", Toast.LENGTH_SHORT).show();
            finish();
        }
    };

    private ResultCallBack<MappedReport> mappedReportResultCallBack = new ResultCallBack<MappedReport>() {
        @Override
        public void onResultCallBack(final MappedReport object, Exception e) {

            if (object.getData() !=null) {
                progressBar_layout.setVisibility(View.GONE);
                LinearLayout mLinearLayout = findViewById(R.id.linear1);
                for (int k = 0; k < object.getData().size(); k++) {

                    // Create Checkbox Dynamically

                    CheckBox checkBox = new CheckBox(new ContextThemeWrapper(AddReports.this, R.style.CheckBoxTheme));
                    if (object.getData().get(k).getName() !=null) {
                        checkBox.setText(object.getData().get(k).getName());
                    }else {
                        checkBox.setText("NA");
                    }

                    checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                int id = 0;
                                for (int m=0; m<object.getData().size();m++) {
                                    if (buttonView.getText().toString().equals(object.getData().get(m).getName())) {
                                        id = object.getData().get(m).getId();
                                    }
                                }
                             listofSelectedIds.add(id);
                            } else {
                                int id = 0;
                                for (int m=0; m<object.getData().size();m++) {
                                    if (buttonView.getText().toString().equals(object.getData().get(m).getName())) {
                                        id = object.getData().get(m).getId();
                                    }
                                }
                                listofSelectedIds.remove(listofSelectedIds.indexOf(id));
                            }
                            String msg = "You have " + (isChecked ? "checked" : "unchecked") + " this Check it Checkbox.";

                            Toast.makeText(AddReports.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Add Checkbox to LinearLayout
                    if (mLinearLayout != null) {
                        mLinearLayout.addView(checkBox);
                    }

                }
            }else {
                progressBar_layout.setVisibility(View.GONE);
                Toast.makeText(AddReports.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reports);

        reportId = getIntent().getIntExtra(IntentConstant.REPORTID, 0);
        reportName = getIntent().getStringExtra(IntentConstant.FILTER_NAME);


        ButterKnife.bind(this);
        App.getInternetComponent().inject(this);
        setSupportActionBar(toolbar);
        setUpMenuComponent();

        getMappedReportData(reportId);

    }



    private void setUpMenuComponent() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP);
            getSupportActionBar().setCustomView(R.layout.menu_report_type_list);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            View menu = (getSupportActionBar().getCustomView());
            toolbarTitle = menu.findViewById(R.id.toolbar_title);
            toolbarTitle.setText(getString(R.string.add_report_to)+" "+reportName);
        }
    }

    public void getMappedReportData(int report_id) {
        progressBar_layout.setVisibility(View.VISIBLE);
        connection.getMappedReport(report_id,mappedReportResultCallBack);
    }

    @OnClick(R.id.add)
    public void add(){

        ArrayListIds arrayListIds = new ArrayListIds(listofSelectedIds);
        connection.postReportCategory(reportId,arrayListIds,reportCategoryResultCallBack);
    }
}
