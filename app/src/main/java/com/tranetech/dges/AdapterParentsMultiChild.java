package com.tranetech.dges;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by HIREN AMALIYAR on 27-05-2017.
 */

class AdapterParentsMultiChild extends RecyclerView.Adapter<AdapterParentsMultiChild.ParentsMultiChildViewHolder> {
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
    public void onBindViewHolder(AdapterParentsMultiChild.ParentsMultiChildViewHolder holder, int position) {
        ParentChildData parentChildData = parentChildDataList.get(position);
        holder.txt_sname.setText(parentChildData.getsName());
        holder.txt_standard.setText(parentChildData.getsStandard());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ParentsMultiChildViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_sname;
        private TextView txt_standard;

        public ParentsMultiChildViewHolder(View itemView) {
            super(itemView);
            txt_sname = (TextView) itemView.findViewById(R.id.txt_sname);
            txt_standard = (TextView) itemView.findViewById(R.id.txt_standard);
        }
    }
}
