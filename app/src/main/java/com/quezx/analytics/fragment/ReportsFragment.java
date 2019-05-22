package com.quezx.analytics.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.quezx.analytics.App;
import com.quezx.analytics.R;
import com.quezx.analytics.adapter.ReportsCategoryAdapter;
import com.quezx.analytics.common.helper.JsonConverter;
import com.quezx.analytics.common.helper.UIHelper;
import com.quezx.analytics.connectivity.AnalyticsConnection;
import com.quezx.analytics.listener.ResultCallBack;
import com.quezx.analytics.model.Reports;
import com.quezx.analytics.model.innerModel.Datum;
import com.quezx.analytics.ui.user.LoginActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportsFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    View progressBar;
    @BindView(R.id.search_block)
    View searchBlock;
    @BindView(R.id.search)
    AppCompatEditText searchBar;
    AppCompatTextView toolbarTitle;
    @BindView(R.id.nodatafound)
    View nodatafound;
    @BindView(R.id.ifdatafound)
    View ifdatafound;


    @Inject
    JsonConverter json;
    @Inject
    UIHelper uiHelper;
    @Inject
    AnalyticsConnection connection;


    private ReportsCategoryAdapter adapter;
    private Context context;

    private ResultCallBack<Reports> reportsListCallBack = new ResultCallBack<Reports>() {
        @Override
        public void onResultCallBack(Reports object, Exception e) {

            Datum datum = new Datum();
            datum.setName("Add New");

            if (object.getData() != null) {
                object.getData().add(datum);
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter.getReports().clear();
                adapter.getReports().addAll(object.getData());

                adapter.notifyDataSetChanged();
            }else {
                progressBar.setVisibility(View.GONE);
              //  search_block.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    };



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) { super.onCreate(savedInstanceState);
    }

    public static ReportsFragment newInstance() {
        return new ReportsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.reports,container,false);
        ButterKnife.bind(this,rootView);
        App.getInternetComponent().inject(this);
        fetchReports();
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setUpMenuComponent();

        adapter = new ReportsCategoryAdapter(getActivity(),uiHelper);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


        return rootView;
    }


        private void setUpMenuComponent() {
            if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.menu_reports_list);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(true);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
                View menu = ((AppCompatActivity) getActivity()).getSupportActionBar().getCustomView();
                toolbarTitle = menu.findViewById(R.id.toolbar_title);

            }
        }

        private void fetchReports() {

            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            connection.getReportList(reportsListCallBack);
        }

}
