package com.tranetech.dges.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tranetech.dges.R;
import com.tranetech.dges.seter_geter.HomeworkData;
import com.tranetech.dges.seter_geter.InquiryData;

import java.util.List;

/**
 * Created by DHWANI-ANDROID on 08-06-17.
 */

public class InquiryAdapter extends RecyclerView.Adapter<InquiryAdapter.InquiryViewHolder> {

    private List<InquiryData> alInquieyData;
    private Context context;

    public InquiryAdapter(List<InquiryData> alInquieyData, Context context) {
        this.alInquieyData = alInquieyData;
        this.context = context;
    }


    @Override
    public InquiryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_inquiry, parent, false);

        return new InquiryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InquiryViewHolder holder, int position) {

        InquiryData InquiryData = alInquieyData.get(position);

        holder.txtInquiry.setText(InquiryData.getsInquiryenquiry());
        holder.txt_date.setText(InquiryData.getsInquirydate());
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return alInquieyData.size();
    }


    public void clear() {
        alInquieyData.clear();
        notifyDataSetChanged();
    }

    public void addALL(List<InquiryData> alInquieyData) {
        this.alInquieyData.addAll(alInquieyData);
        notifyDataSetChanged();
    }


    public class InquiryViewHolder extends RecyclerView.ViewHolder {
        public TextView txtInquiry, txt_date;


        public InquiryViewHolder(View itemView) {
            super(itemView);
            txtInquiry = (TextView) itemView.findViewById(R.id.txt_inquiry);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
        }
    }
}

