package com.tranetech.dges.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tranetech.dges.R;
import com.tranetech.dges.activities.ActivityMainDashBord;
import com.tranetech.dges.seter_geter.FeesData;
import com.tranetech.dges.seter_geter.HomeworkData;
import com.tranetech.dges.seter_geter.ParentChildData;

import java.util.List;

/**
 * Created by HIREN AMALIYAR on 27-05-2017.
 */

public class AdapterParentsMultiChild extends RecyclerView.Adapter<AdapterParentsMultiChild.ParentsMultiChildViewHolder> {
    private Context context;
    private List<ParentChildData> parentChildDataList;

    public AdapterParentsMultiChild(List<ParentChildData> parentChildDataList, Context context) {
        this.context = context;
        this.parentChildDataList = parentChildDataList;
    }

    @Override
    public AdapterParentsMultiChild.ParentsMultiChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_parents_multi_stu, parent, false);
        return new AdapterParentsMultiChild.ParentsMultiChildViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterParentsMultiChild.ParentsMultiChildViewHolder holder, final int position) {
        try {
            final ParentChildData parentChildData = parentChildDataList.get(position);

            Log.e("onBindViewHolder: ", parentChildData.getsName());

            holder.txt_sname.setText(parentChildData.getsName() + " " + parentChildData.getmName() + " " + parentChildData.getlName());
            holder.txt_standard.setText(parentChildData.getsStandard());


            final String stu_name = parentChildData.getsName() + " " + parentChildData.getmName() + " " + parentChildData.getlName();

            holder.lr_multi_students_selection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ActivityMainDashBord.class);
                    intent.putExtra("stu_id", parentChildData.getsStudentID());
                    intent.putExtra("stu_name", stu_name);
                    intent.putExtra("photo", parentChildData.getPhoto());
                    intent.putExtra("std_ID", parentChildData.getsStandard_ID());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(intent);

                }
            });


            Glide.with(context)
                    .load(parentChildData.getPhoto())
                    .centerCrop()
                    .crossFade()
                    .into(holder.imgStudentProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return parentChildDataList.size();
    }


    public class ParentsMultiChildViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout lr_multi_students_selection;
        private TextView txt_sname;
        private TextView txt_standard;
        private ImageView imgStudentProfile;

        public ParentsMultiChildViewHolder(View itemView) {
            super(itemView);
            txt_sname = (TextView) itemView.findViewById(R.id.txt_sname);
            lr_multi_students_selection = (LinearLayout) itemView.findViewById(R.id.lr_multi_students_selection);
            txt_standard = (TextView) itemView.findViewById(R.id.txt_standard);
            imgStudentProfile = (ImageView) itemView.findViewById(R.id.img_student_profile);
        }
    }

    public void clear() {
        parentChildDataList.clear();
        notifyDataSetChanged();
    }


    public void addALL(List<ParentChildData> parentChildDataList) {
        this.parentChildDataList.addAll(parentChildDataList);
        notifyDataSetChanged();
    }


}
