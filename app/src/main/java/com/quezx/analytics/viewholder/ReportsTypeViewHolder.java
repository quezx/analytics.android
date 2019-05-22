package com.quezx.analytics.viewholder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.quezx.analytics.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportsTypeViewHolder extends RecyclerView.ViewHolder {

    @Nullable
    @BindView(R.id.reportName)
    public AppCompatTextView reportName;

    public ReportsTypeViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
