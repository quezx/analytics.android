package com.quezx.analytics.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.quezx.analytics.App;
import com.quezx.analytics.R;
import com.quezx.analytics.common.constants.IntentConstant;
import com.quezx.analytics.connectivity.AnalyticsConnection;
import com.quezx.analytics.listener.ResultCallBack;
import com.quezx.analytics.model.UserDashBoard;
import com.quezx.analytics.model.innerModel.UserDashBoardData;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;

public class ChangeDashboards extends AppCompatActivity {

    @BindView(R.id.linear1)
    LinearLayout linearLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    AppCompatTextView toolbarTitle;

    @Inject
    AnalyticsConnection connection;

    @BindView(R.id.setAsDefault)
    Button setAsDefault;

    UserDashBoard userDashBoard = new UserDashBoard();

    int selectedIdofDashBoard;

    private ResultCallBack<ResponseBody> responseBodyResultCallBack = new ResultCallBack<ResponseBody>() {
        @Override
        public void onResultCallBack(ResponseBody object, Exception e) {
            try {
                if (object.string().equals("OK")) {
                    Toast.makeText(ChangeDashboards.this, "CHANGED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                    finish();
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }



        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_dashboards);

        ButterKnife.bind(this);
        App.getInternetComponent().inject(this);
        setSupportActionBar(toolbar);
        setUpMenuComponent();

        Gson gson = new Gson();
        userDashBoard = gson.fromJson(getIntent().getStringExtra(IntentConstant.CHANGEDASHBOARDDATA), UserDashBoard.class);


        if (userDashBoard !=null) {
           createTheRadioButton();
        }

    }


    public void createTheRadioButton() {
        final List<UserDashBoardData> listofdashboard = userDashBoard.getData();
        LinearLayout mLinearLayout = findViewById(R.id.linear1);


            // create radio button
            final RadioButton[] rb = new RadioButton[listofdashboard.size()];
            final RadioGroup rg = new RadioGroup(this);
            rg.setOrientation(RadioGroup.VERTICAL);
            for (int i = 0; i < listofdashboard.size(); i++) {
                rb[i] = new RadioButton(new ContextThemeWrapper(this, R.style.CheckBoxTheme));
                rg.addView(rb[i]);
                if (listofdashboard.get(i).getDashboard().getName() !=null) {
                    rb[i].setText(listofdashboard.get(i).getDashboard().getName());
                }else {
                    rb[i].setText("");
                }
            }
            mLinearLayout.addView(rg);

            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    for (int j= 0;j<rg.getChildCount();j++) {
                        RadioButton btn = (RadioButton) rg.getChildAt(j);
                        if (btn.getId() == checkedId) {
                            String txt = (String) btn.getText();
                            for (int i = 0; i < listofdashboard.size(); i++)
                            if (txt.equals(listofdashboard.get(i).getDashboard().getName()))
                                selectedIdofDashBoard = listofdashboard.get(i).getId();
                            return;
                        }

                    }
                }
            });



    }

    private void setUpMenuComponent() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP);
            getSupportActionBar().setCustomView(R.layout.menu_report_type_list);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            View menu = (getSupportActionBar().getCustomView());
            toolbarTitle = menu.findViewById(R.id.toolbar_title);
            toolbarTitle.setText(getString(R.string.title_dashboards));
            //TODO SET TITLE COMING FROM INTENT toolbarTitle.setText(name);

        }
    }

    @OnClick(R.id.setAsDefault)
    public void setAsDefault() {
        connection.setAsDefaultDashBoard(selectedIdofDashBoard, responseBodyResultCallBack);
    }
}
