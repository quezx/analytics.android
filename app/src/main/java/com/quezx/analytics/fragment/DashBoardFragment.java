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
import android.webkit.WebView;
import android.widget.Toast;

import com.quezx.analytics.App;
import com.quezx.analytics.R;
import com.quezx.analytics.common.constants.ActivityRequestCodes;
import com.quezx.analytics.common.constants.IntentConstant;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashBoardFragment extends Fragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.webView)
    WebView webView;

    AppCompatTextView toolbarTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) { super.onCreate(savedInstanceState);
    }

    public static DashBoardFragment newInstance() {
        return new DashBoardFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.dashboard,container,false);

        ButterKnife.bind(this, rootView);
        App.getInternetComponent().inject(this);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        webView.loadUrl("http://www.example.com");
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
        switch ( id ) {
            case R.id.menu_share:
                Toast.makeText(getActivity(), "asdjklhfkasjhdsklaj", Toast.LENGTH_SHORT).show();
                //TODO WRITE CODE OR SHARE

        }

        return super.onOptionsItemSelected(item);
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
}
