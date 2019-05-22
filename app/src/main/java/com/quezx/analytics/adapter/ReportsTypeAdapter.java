package com.quezx.analytics.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.quezx.analytics.R;
import com.quezx.analytics.common.constants.CardTypes;
import com.quezx.analytics.common.constants.IntentConstant;
import com.quezx.analytics.common.helper.UIHelper;
import com.quezx.analytics.model.innerModel.ReportData;
import com.quezx.analytics.ui.activity.DetailReportPage;
import com.quezx.analytics.viewholder.ProgressBarHolder;
import com.quezx.analytics.viewholder.ReportsTypeViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ReportsTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ReportsTypeViewHolder holder;
    private String searchKey = null;
    private List<ReportData> reports = new ArrayList<>();
    private List<ReportData> displayreports = new ArrayList<>();
    private final UIHelper uiHelper;

    public ReportsTypeAdapter(Context context, UIHelper uiHelper) {
        this.context = context;
        this.uiHelper = uiHelper;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cardView;
        RecyclerView.ViewHolder baseViewHolder;
        if (viewType == CardTypes.CARD_CLIENT) {
            cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_reports_type,
                    parent, false);
            baseViewHolder = new ReportsTypeViewHolder(cardView);
        } else {
            cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_progress_bar,
                    parent, false);
            baseViewHolder = new ProgressBarHolder(cardView);
        }
        return baseViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int pos) {



        holder = (ReportsTypeViewHolder) viewHolder;
        final ReportData report;
        if (searchKey != null) {
            report = displayreports.get(pos);
        }else {
            report = reports.get(pos);
        }

        if (report.getReport().getName() !=null) {
            holder.reportName.setText(report.getReport().getName());
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, DetailReportPage.class);
                i.putExtra(IntentConstant.REPORTID,report.getReport_id());
                i.putExtra(IntentConstant.QUESTIONID,report.getReport().getQuestion_id());
                context.startActivity(i);

                Toast.makeText(context, "CLIKCED", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        if (searchKey !=null && !searchKey.isEmpty()) {
            return displayreports.size();
        } else {
            return reports.size();
        }
    }

    public List<ReportData> getReports() {return reports;}

    @Override
    public int getItemViewType(int position) {
        if (reports.get(position).getReport_id() == CardTypes.CARD_LOADING) return CardTypes.CARD_LOADING;
        return CardTypes.CARD_CLIENT;
    }
}
