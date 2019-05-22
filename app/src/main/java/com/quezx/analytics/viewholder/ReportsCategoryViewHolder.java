package com.quezx.analytics.viewholder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.quezx.analytics.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportsCategoryViewHolder extends RecyclerView.ViewHolder {

    @Nullable
    @BindView(R.id.report_types_name)
    public AppCompatTextView report_types_name;

    @Nullable
    @BindView(R.id.icon)
    public ImageView icon;

    public ReportsCategoryViewHolder( View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
