package com.tranetech.dges;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Markand on 26-05-2017 at 10:51 AM.
 */

public class CircularAdapter extends RecyclerView.Adapter<CircularAdapter.CircularViewHolder> {

    private List<CircularData> alCircularData;
    private Context context;

    public CircularAdapter(List<CircularData> alCircularData,Context context)
    {
        this.alCircularData = alCircularData;
        this.context = context;
    }

    @Override
    public CircularViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_circular, parent, false);

        return new CircularViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CircularViewHolder holder, int position) {
        final CircularData circularData = alCircularData.get(position);
        holder.txtCircularTitle.setText(circularData.getsCircularTitle());
        holder.txtCircularDesc.setText(circularData.getsCircularDesc());
        holder.txtCircularDate.setText(circularData.getsCircualarDate());
    }

    @Override
    public int getItemCount() {
        return alCircularData.size();
    }

    public class CircularViewHolder extends RecyclerView.ViewHolder{
        public TextView txtCircularTitle,txtCircularDesc,txtCircularDate;
        public ImageView imgCircualr;

        public CircularViewHolder(View itemView) {
            super(itemView);
            txtCircularTitle = (TextView)itemView.findViewById(R.id.txt_circular_title);
            txtCircularDesc = (TextView)itemView.findViewById(R.id.txt_circular_description);
            txtCircularDate = (TextView)itemView.findViewById(R.id.txt_circular_date);
            imgCircualr = (ImageView)itemView.findViewById(R.id.img_circular);
        }
    }
}
