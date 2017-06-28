package com.tranetech.dges.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tranetech.dges.R;
import com.tranetech.dges.seter_geter.FeesData;

import java.util.List;

/**
 * Created by DHWANI-ANDROID on 27-05-17.
 */


/**
 * Modified by Hiren-ANDROID on 05-06-17.
 */
public class FeesAdapter extends RecyclerView.Adapter<FeesAdapter.FeesViewHolder> {

    private List<FeesData> alFeesData;
    private Context context;
    private TextView txt_total_fees, due_rup_txt, total_paid_txt;
    private String t_paid, t_FEES;
    private int sum_of_paid = 0;

    public FeesAdapter(List<FeesData> alFessData, Context context, TextView txt_total_fees, TextView total_paid_txt, TextView due_rup_txt) {
        this.alFeesData = alFessData;
        this.context = context;
        this.txt_total_fees = txt_total_fees;
        this.total_paid_txt = total_paid_txt;
        this.due_rup_txt = due_rup_txt;

    }

    @Override
    public FeesAdapter.FeesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_fees, parent, false);

        return new FeesAdapter.FeesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeesAdapter.FeesViewHolder holder, int position) {


        try {
            final FeesData FeesData = alFeesData.get(position);

            holder.txtFeesDue.setText(FeesData.getsFeesMonth());
            holder.txtFeesPaid.setText(FeesData.getsFeesPaid());
            switch (FeesData.getsFeesMonth()){
                case "01" :
                    holder.txt_month.setText("January");
                    break;
                case "02" :
                    holder.txt_month.setText("February");
                    break;
                case "03" :
                    holder.txt_month.setText("March");
                    break;
                case "04" :
                    holder.txt_month.setText("April");
                    break;
                case "05" :
                    holder.txt_month.setText("May");
                    break;
                case "06" :
                    holder.txt_month.setText("June");
                    break;
                case "07" :
                    holder.txt_month.setText("July");
                    break;
                case "08" :
                    holder.txt_month.setText("August");
                    break;
                case "09" :
                    holder.txt_month.setText("September");
                    break;
                case "10" :
                    holder.txt_month.setText("October");
                    break;
                case "11" :
                    holder.txt_month.setText("November");
                    break;
                case "12" :
                    holder.txt_month.setText("December");
                    break;
            }
            holder.txt_stu_name.setText(FeesData.getsFeesFname());
            holder.txt_paymode.setText(FeesData.getsFeesPaymode());


            String str_check_no = FeesData.getsFeesCheck();

            if (str_check_no == "null" || str_check_no.isEmpty()) {
                str_check_no = "Data not available";
            }

            String str_bank = FeesData.getsFeesBank();

            if (str_bank == "null" || str_bank.isEmpty()) {
                str_bank = "Data not available";
            }


            holder.txt_chk_no.setText(str_check_no);
            holder.txt_bank_name.setText(str_bank);

            //for header fees activity
            txt_total_fees.setText(FeesData.getsFeesTotal());

//            for (int i = 0; i < getItemCount() - 1; i++) {

               // FeesData FeesData_cal = alFeesData.get(i);

                t_paid = FeesData.getsFeesPaid();
                t_FEES = FeesData.getsFeesTotal();


//                if (!t_paid.isEmpty() || t_paid != null || !t_FEES.isEmpty() || t_FEES != null) {
//
//
//                    if (Integer.parseInt(t_paid) < Integer.parseInt(t_FEES)) {

                        sum_of_paid = sum_of_paid + Integer.parseInt(t_paid);
//                    }
//
//                } else {
//                    t_paid = "no data";
//                    t_FEES = "no data";
//                }
//            }

            String Gross_Due, Gross_Paid;

            Gross_Paid = String.valueOf(sum_of_paid);
            Gross_Due = String.valueOf(Integer.parseInt(t_FEES) - Integer.parseInt(Gross_Paid));

            due_rup_txt.setText(Gross_Due);
            total_paid_txt.setText(Gross_Paid);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return alFeesData.size();
    }

    public void clear() {
        alFeesData.clear();
        notifyDataSetChanged();
    }

    public void addALL(List<FeesData> alFeesData) {
        this.alFeesData.addAll(alFeesData);
        notifyDataSetChanged();
    }

    public class FeesViewHolder extends RecyclerView.ViewHolder {
        public TextView txtFeesDue, txtFeesPaid, txt_month, txt_chk_no, txt_bank_name, txt_stu_name, txt_paymode;
        public ImageView imgFees;

        public FeesViewHolder(View itemView) {
            super(itemView);

            txtFeesDue = (TextView) itemView.findViewById(R.id.rup_txt);
            txtFeesPaid = (TextView) itemView.findViewById(R.id.paid_rs_txt);
            txt_month = (TextView) itemView.findViewById(R.id.txt_month);
            imgFees = (ImageView) itemView.findViewById(R.id.img_fees);
            txt_chk_no = (TextView) itemView.findViewById(R.id.txt_chk_no);
            txt_bank_name = (TextView) itemView.findViewById(R.id.txt_bank_name);
            txt_stu_name = (TextView) itemView.findViewById(R.id.txt_stu_name);
            txt_paymode = (TextView) itemView.findViewById(R.id.txt_paymode);

        }
    }


}

