package com.tranetech.dges.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.android.caching.FileCacher;
import com.tranetech.dges.R;
import com.tranetech.dges.activities.ActivityMainDashBord;
import com.tranetech.dges.activities.ActivityParentsMultiChild;
import com.tranetech.dges.seter_geter.ParentChildData;

import java.io.IOException;
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
        ParentChildData parentChildData = parentChildDataList.get(position);

        Log.e("onBindViewHolder: ", parentChildData.getsName());

        holder.txt_sname.setText(parentChildData.getsName() + " " + parentChildData.getmName() + " " + parentChildData.getlName());
        holder.txt_standard.setText(parentChildData.getsStandard());

        holder.lr_multi_students_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ActivityMainDashBord.class);
                intent.putExtra("position", position);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return parentChildDataList.size();
    }

    public class ParentsMultiChildViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout lr_multi_students_selection;
        private TextView txt_sname;
        private TextView txt_standard;

        public ParentsMultiChildViewHolder(View itemView) {
            super(itemView);
            txt_sname = (TextView) itemView.findViewById(R.id.txt_sname);
            lr_multi_students_selection = (LinearLayout) itemView.findViewById(R.id.lr_multi_students_selection);
            txt_standard = (TextView) itemView.findViewById(R.id.txt_standard);
        }
    }


}
