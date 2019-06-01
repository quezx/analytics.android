package com.quezx.analytics.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.quezx.analytics.App;
import com.quezx.analytics.R;
import com.quezx.analytics.common.constants.ActivityRequestCodes;
import com.quezx.analytics.common.constants.IntentConstant;
import com.quezx.analytics.common.constants.ServerConstant;
import com.quezx.analytics.connectivity.AnalyticsConnection;
import com.quezx.analytics.listener.ResultCallBack;
import com.quezx.analytics.model.UserDashBoard;
import com.quezx.analytics.model.innerModel.Dashboard;
import com.quezx.analytics.model.innerModel.UserDashBoardData;
import com.quezx.analytics.ui.activity.ChangeDashboards;
import com.quezx.analytics.ui.activity.ReportTypeActivity;
import com.quezx.analytics.ui.activity.ShareDashboard;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashBoardFragment extends Fragment {
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

    @BindView(R.id.changeDashboard)
    LinearLayout linearLayoutChangeDashboard;

    @BindView(R.id.dashboard_name)
    TextView dashboard_name;

    UserDashBoard listofUserDashboard = new UserDashBoard();

    UserDashBoardData dashBoardData = new UserDashBoardData();

    String recievedURL;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static DashBoardFragment newInstance() {
        return new DashBoardFragment();
    }

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

    private ResultCallBack<UserDashBoard> listofDashboard = new ResultCallBack<UserDashBoard>() {
        @Override
        public void onResultCallBack(UserDashBoard object, Exception e) {
            listofUserDashboard = object;
            Dashboard dashboard = new Dashboard();
            for (int i = 0; i < object.getData().size(); i++) {
                if (object.getData().get(i).getIs_default()) {
                    dashBoardData = object.getData().get(i);
                }
            }

            if (dashBoardData.getDashboard() !=null) {
                webView.setVisibility(View.VISIBLE);
                getDashboardURL(dashBoardData.getDashboard().getDId(), dashBoardData.getDashboard().getDashboard_id());
                dashboard_name.setText(dashBoardData.getDashboard().getName());
            }else {
               webView.setVisibility(View.GONE);
               dashboard_name.setText(getString(R.string.clicktoselect));
            }


        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.dashboard, container, false);

        ButterKnife.bind(this, rootView);
        App.getInternetComponent().inject(this);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        getUserDashboard();
        setUpMenuComponent();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dashboardmenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent i;
        switch (id) {

            case R.id.menu_share:
              shareTextUrl();
              //  startActivity(new Intent(getActivity(), ShareDashboard.class));
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
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.custom_title_dashboard);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
            View menu = ((AppCompatActivity) getActivity()).getSupportActionBar().getCustomView();
            toolbarTitle = menu.findViewById(R.id.toolbar_title);

        }
    }

    public void getUserDashboard() {
        connection.getUserDashBoard(listofDashboard);
    }

    public void getDashboardURL(int id, int dashboard_id) {
        connection.getDashboardURL(id, dashboard_id, ServerConstant.METABASE_URL, stringResultCallBack);
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

    @OnClick(R.id.changeDashboard)
    public void changeDashboard() {
        sendDatatoChangeDashBoard();
    }


    public void sendDatatoChangeDashBoard() {
        Gson gson = new Gson();
        String myJson = gson.toJson(listofUserDashboard);
        Intent intent = new Intent(getActivity(), ChangeDashboards.class);
        intent.putExtra(IntentConstant.CHANGEDASHBOARDDATA, myJson);
        startActivityForResult(intent, IntentConstant.ACTIVITYRESULTDATA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntentConstant.ACTIVITYRESULTDATA) {
            getUserDashboard();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}