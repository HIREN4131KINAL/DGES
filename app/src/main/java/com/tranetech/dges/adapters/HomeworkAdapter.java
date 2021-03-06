package com.tranetech.dges.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tranetech.dges.seter_geter.HomeworkData;
import com.tranetech.dges.R;

import java.util.List;

/**
 * Created by Markand on 26-05-2017 at 09:23 AM.
 */

public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.HomeworkViewHolder> {

    private List<HomeworkData> alHWData;
    private Context context;

    public HomeworkAdapter(List<HomeworkData> alHWData, Context context) {
        this.alHWData = alHWData;
        this.context = context;
    }

    @Override
    public HomeworkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_homework, parent, false);

        return new HomeworkViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HomeworkViewHolder holder, int position) {

        try {
            HomeworkData hwData = alHWData.get(position);
            holder.txt_teacher.setText("(" + hwData.getTeachers() + ")");
            holder.txtSubName.setText(hwData.getsSubName());
            holder.txtHWDate.setText(hwData.getsHWDate());
            holder.txtHWDescription.setText(hwData.getsHWDescription());
            holder.imgHW.setImageResource(R.drawable.homework);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        return alHWData.size();
    }

    public class HomeworkViewHolder extends RecyclerView.ViewHolder {
        public TextView txtSubName, txtHWDate, txtHWDescription, txt_teacher;
        public ImageView imgHW;

        public HomeworkViewHolder(View itemView) {
            super(itemView);
            txtSubName = (TextView) itemView.findViewById(R.id.txt_sub_name);
            txtHWDescription = (TextView) itemView.findViewById(R.id.txt_hw_desc);
            txtHWDate = (TextView) itemView.findViewById(R.id.txt_hw_date);
            imgHW = (ImageView) itemView.findViewById(R.id.img_hw);
            txt_teacher = (TextView) itemView.findViewById(R.id.txt_teacher);
        }
    }

    public void clear() {
        alHWData.clear();
        notifyDataSetChanged();
    }

    public void addALL(List<HomeworkData> alHWData) {
        this.alHWData.addAll(alHWData);
        notifyDataSetChanged();
    }

}
