package com.tranetech.dges.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kosalgeek.android.caching.FileCacher;
import com.tranetech.dges.R;
import com.tranetech.dges.adapters.FeesAdapter;
import com.tranetech.dges.seter_geter.FeesData;
import com.tranetech.dges.utils.ErrorAlert;
import com.tranetech.dges.utils.GetIP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityFees extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private List<FeesData> feesDatas = new ArrayList<>();
    private FileCacher<List<FeesData>> fileCacherFess;
    private RecyclerView recyclerView;
    private String StudentId;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FeesAdapter feesAdapter;
    private Intent mIntent;
    private TextView txt_total_fees, total_paid_txt, due_rup_txt;
    private TextView txtEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sr_fee);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_fees);
        txt_total_fees = (TextView) findViewById(R.id.txt_total_fees);
        txtEmpty = (TextView) findViewById(R.id.txt_fees_empty);

        total_paid_txt = (TextView) findViewById(R.id.total_paid_txt);
        due_rup_txt = (TextView) findViewById(R.id.due_rup_txt);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Fees");

        mIntent = getIntent();
        StudentId = mIntent.getStringExtra("sid");

        fileCacherFess = new FileCacher<>(ActivityFees.this, "stu_fees_" + StudentId + ".txt");

    }

    @Override
    public void onRefresh() {
        if (feesAdapter != null) {
            feesAdapter.clear();
            getData(StudentId);
            feesAdapter.addALL(feesDatas);
            swipeRefreshLayout.setRefreshing(false);
        } else {
            ErrorAlert.error("No data available,please add employee data from employee list", ActivityFees.this);
        }
    }

    @Override
    protected void onResume() {
       /* if (feesAdapter != null) {
            feesAdapter.clear();
            getData(StudentId);
            feesAdapter.addALL(feesDatas);
            swipeRefreshLayout.setRefreshing(false);
        }*/

        if (feesAdapter != null) {
            feesAdapter.clear();
        }
        getData(StudentId);
        super.onResume();
    }

    private void getData(final String StudentId) {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading data...", "Please wait...", false, false);
        GetIP getIP = new GetIP();
        String strUrl = getIP.updateip("fees.php");
        StringRequest postRequest = new StringRequest(Request.Method.POST, strUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response StudentAll data : ", response);
                        if (feesAdapter != null) {
                            feesAdapter.clear();
                        }
                        loading.dismiss();
                        try {
                            getJson(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();

                        try {
                            feesDatas = fileCacherFess.readCache();
                            IntialAdapter();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        String message = null;
                        if (volleyError instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please reset your connection!";
                        } else if (volleyError instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                        } else if (volleyError instanceof AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (volleyError instanceof NoConnectionError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }
                        ErrorAlert.error(message, ActivityFees.this);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("sid", StudentId);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);
    }


    private void getJson(String response) throws JSONException, IOException {

        JSONObject jsonObject = new JSONObject(response);

        String res = jsonObject.getString("list");
        if (res.equals("0")) {
//            ErrorAlert.error("No Data Available", ActivityResult.this);
            swipeRefreshLayout.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
        } else {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                FeesData FeesData = new FeesData();

                JSONObject jobj = jsonArray.getJSONObject(i);
                FeesData.setsFeesDue(jobj.getString("rAmount"));
                FeesData.setsFeesPaid(jobj.getString("pAmount"));
                FeesData.setsFeesMonth(jobj.getString("date"));
                FeesData.setsFeesFname(jobj.getString("fName"));
                FeesData.setsFeesCheck(jobj.getString("cNo"));
                FeesData.setsFeesBank(jobj.getString("bName"));
                FeesData.setsFeesPaymode(jobj.getString("paymentMode"));
                FeesData.setsFeesTotal(jobj.getString("totalFees"));

                feesDatas.add(FeesData);
                fileCacherFess.writeCache(feesDatas);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                txtEmpty.setVisibility(View.GONE);
            }
        }
        IntialAdapter();
    }


    public void IntialAdapter() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);

        // allows for optimizations if all item views are of the same size:
        recyclerView.setHasFixedSize(true);
        feesAdapter = new FeesAdapter(feesDatas, this, txt_total_fees, total_paid_txt, due_rup_txt);
       /* recyclerView.scrollToPosition(feesDatas.size() + 1);
        feesAdapter.notifyItemInserted(feesDatas.size() + 1);*/
        recyclerView.setAdapter(feesAdapter);

    }

}



