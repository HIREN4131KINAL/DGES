package com.tranetech.dges.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tranetech.dges.R;
import com.tranetech.dges.seter_geter.ResultData;

import java.util.List;

/**
 * Created by DHWANI-ANDROID on 27-05-17.
 */

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {

    private List<ResultData> alResultData;
    private Context context;

    public ResultAdapter(List<ResultData> alResultData,Context context)

    {
        this.alResultData = alResultData;
        this.context = context;
    }

    @Override
    public ResultAdapter.ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_result, parent, false);

        return new ResultAdapter.ResultViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ResultAdapter.ResultViewHolder holder, int position) {
        final ResultData ResultData = alResultData.get(position);
        holder.txtResultSub.setText(ResultData.getsResultSub());
        holder.txtResultObtMarks.setText(ResultData.getsResultObtMarks());
        holder.txtResultSlash.setText(ResultData.getsResultSlash());
        holder.txtResultOutMarks.setText(ResultData.getsResultOutMarks());
    }

    @Override
    public int getItemCount() {
        return alResultData.size();
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder{

        public TextView txtResultSub,txtResultObtMarks,txtResultSlash,txtResultOutMarks;


        public ResultViewHolder(View itemView) {
            super(itemView);
            txtResultSub = (TextView)itemView.findViewById(R.id.sub_txt);
            txtResultObtMarks = (TextView)itemView.findViewById(R.id.marks_txt1);
            txtResultSlash = (TextView)itemView.findViewById(R.id.marks_txt2);
            txtResultOutMarks = (TextView)itemView.findViewById(R.id.marks_txt3);
        }
    }
}

