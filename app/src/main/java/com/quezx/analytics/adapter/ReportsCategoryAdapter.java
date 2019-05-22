package com.quezx.analytics.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import com.quezx.analytics.model.innerModel.Datum;
import com.quezx.analytics.ui.activity.AddCategory;
import com.quezx.analytics.ui.activity.ReportTypeActivity;
import com.quezx.analytics.viewholder.ProgressBarHolder;
import com.quezx.analytics.viewholder.ReportsCategoryViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ReportsCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ReportsCategoryViewHolder holder;
    private String searchKey = null;
    private List<Datum> reports = new ArrayList<>();
    private List<Datum> displayreports = new ArrayList<>();
    private final UIHelper uiHelper;

    public ReportsCategoryAdapter(Context context, UIHelper uiHelper) {
        this.context = context;
        this.uiHelper = uiHelper;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cardView;
        RecyclerView.ViewHolder baseViewHolder;
        if (viewType == CardTypes.CARD_CLIENT) {
            cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_reports_category,
                    parent, false);
            baseViewHolder = new ReportsCategoryViewHolder(cardView);
        } else {
            cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_progress_bar,
                    parent, false);
            baseViewHolder = new ProgressBarHolder(cardView);
        }
        return baseViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int pos) {



        holder = (ReportsCategoryViewHolder) viewHolder;
        final Datum report;
        if (searchKey != null) {
        report = displayreports.get(pos);
        }else {
        report = reports.get(pos);
        }

        if (report.getName() !=null) {
            holder.report_types_name.setText(report.getName()+"( "+report.getReportCount()+" )");
        }
        if (report.getName() == "Add New") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.icon.setImageDrawable(context.getDrawable(R.drawable.ic_add_24dp));
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  AppCompatTextView compatTextView = (AppCompatTextView)v;

                String str =  reports.get(pos).getName();
                if (str.equals("Add New")){
                    Toast.makeText(context, "ADD NEW CLICKED", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, AddCategory.class);
                    context.startActivity(intent);
                }else {
                    Intent i = new Intent(context, ReportTypeActivity.class);
                    i.putExtra(IntentConstant.REPORTID,report.getId());
                    i.putExtra(IntentConstant.FILTER_NAME,report.getName());
                    context.startActivity(i);

                    Toast.makeText(context, "CLIKCED", Toast.LENGTH_SHORT).show();
                }
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

    public List<Datum> getReports() {return reports;}

    @Override
    public int getItemViewType(int position) {
        if (reports.get(position).getId() == CardTypes.CARD_LOADING) return CardTypes.CARD_LOADING;
        return CardTypes.CARD_CLIENT;
    }
}
