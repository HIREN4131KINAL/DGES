package com.tranetech.dges.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tranetech.dges.R;
import com.tranetech.dges.seter_geter.LeaveData;

import java.util.List;

/**
 * Created by HIREN AMALIYAR on 09-06-2017.
 */

public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.leaveViewHolder> {

    private List<LeaveData> alILeaveData;
    private Context context;

    public LeaveAdapter(List<LeaveData> alILeaveData, Context context) {
        this.alILeaveData = alILeaveData;
        this.context = context;
    }


    @Override
    public LeaveAdapter.leaveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_leave, parent, false);

        return new LeaveAdapter.leaveViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LeaveAdapter.leaveViewHolder holder, int position) {
        final LeaveData LeaveData = alILeaveData.get(position);

        Log.e("onBindViewHolder: ", LeaveData.getsLeavedescription());

        holder.txtLeave.setText(LeaveData.getsLeavedescription());
        holder.txt_date.setText(LeaveData.getsLeavedate());
        if (LeaveData.getsLeavestatus().equals("0")) {
            holder.txt_status.setText("Pending...");
            holder.txt_status.setTextColor(Color.BLUE);
            holder.imgStatus.setImageResource(R.drawable.ic_pending);
        } else if (LeaveData.getsLeavestatus().equals("1")) {
            holder.txt_status.setText("Granted.");
            holder.txt_status.setTextColor(Color.GREEN);
            holder.imgStatus.setImageResource(R.drawable.ic_granted);
        } else {
            holder.txt_status.setText("Cancle.");
            holder.txt_status.setTextColor(Color.RED);
            holder.imgStatus.setImageResource(R.drawable.ic_cancel);
        }


    }

    @Override
    public int getItemCount() {
        return alILeaveData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public void clear() {
        alILeaveData.clear();
        notifyDataSetChanged();
    }

    public void addALL(List<LeaveData> alILeaveData) {
        this.alILeaveData.addAll(alILeaveData);
        notifyDataSetChanged();
    }

    public class leaveViewHolder extends RecyclerView.ViewHolder {
        public TextView txtLeave, txt_date, txt_status;
        public ImageView imgStatus;


        public leaveViewHolder(View itemView) {
            super(itemView);
            txtLeave = (TextView) itemView.findViewById(R.id.txt_leave);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_status = (TextView) itemView.findViewById(R.id.txt_status);
            imgStatus = (ImageView) itemView.findViewById(R.id.img_leave_status);
        }
    }
}
