package com.tranetech.dges.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tranetech.dges.seter_geter.FeesData;
import com.tranetech.dges.R;

import java.util.List;

/**
 * Created by DHWANI-ANDROID on 27-05-17.
 */

public class FeesAdapter extends RecyclerView.Adapter<FeesAdapter.FeesViewHolder> {

    private List<FeesData> alFeesData;
    private Context context;

    public FeesAdapter(List<FeesData> alFessData,Context context)
    {
        this.alFeesData = alFessData;
        this.context = context;
    }

    @Override
    public FeesAdapter.FeesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_fees, parent, false);

        return new FeesAdapter.FeesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeesAdapter.FeesViewHolder holder, int position) {
        final FeesData FeesData = alFeesData.get(position);
        holder.txtFeesAmount.setText(FeesData.getsFeesAmount());
        holder.txtFeesDue.setText(FeesData.getsFeesDue());
        holder.txtFeesPaid.setText(FeesData.getsFeesPaid());
        holder.txtFeesCal.setText(FeesData.getsFeesCal());
    }

    @Override
    public int getItemCount() {
        return alFeesData.size();
    }

    public class FeesViewHolder extends RecyclerView.ViewHolder{
        public TextView txtFeesAmount,txtFeesDue,txtFeesPaid, txtFeesCal;
        public ImageView imgFees;

        public FeesViewHolder(View itemView) {
            super(itemView);
            txtFeesAmount = (TextView)itemView.findViewById(R.id.amount_rup_txt);
            txtFeesDue = (TextView)itemView.findViewById(R.id.rup_txt);
            txtFeesPaid = (TextView)itemView.findViewById(R.id.paid_rs_txt);
            txtFeesCal = (TextView)itemView.findViewById(R.id.cal_txt);
            imgFees = (ImageView)itemView.findViewById(R.id.img_fees);
        }
    }
}

