package com.tranetech.dges.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tranetech.dges.seter_geter.ObservationData;
import com.tranetech.dges.R;

import java.util.List;

/**
 * Created by Markand on 26-05-2017 at 11:33 AM.
 */

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ObservationViewHolder> {
    private List<ObservationData> observationDatas;
    private Context context;

    public ObservationAdapter(List<ObservationData> observationDatas, Context context) {
        this.observationDatas = observationDatas;
        this.context = context;
    }

    @Override
    public ObservationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_observation, parent, false);

        return new ObservationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ObservationViewHolder holder, int position) {
        final ObservationData observationData = observationDatas.get(position);
        holder.txtObStudentName.setText(observationData.getsObStudentName());
        holder.txtObDate.setText(observationData.getsObDate());
        holder.txtObTitle.setText(observationData.getsObTitle());
        holder.txtObDescription.setText(observationData.getsObDesc());
    }

    @Override
    public int getItemCount() {
        return observationDatas.size();
    }

    public class ObservationViewHolder extends RecyclerView.ViewHolder {
        public TextView txtObStudentName, txtObDate, txtObTitle, txtObDescription;

        public ObservationViewHolder(View itemView) {
            super(itemView);
            txtObStudentName = (TextView) itemView.findViewById(R.id.txt_observation_student_name);
            txtObDate = (TextView) itemView.findViewById(R.id.txt_observation_date);
            txtObTitle = (TextView) itemView.findViewById(R.id.txt_observation_title);
            txtObDescription = (TextView) itemView.findViewById(R.id.txt_observation_desc);
        }
    }
}
