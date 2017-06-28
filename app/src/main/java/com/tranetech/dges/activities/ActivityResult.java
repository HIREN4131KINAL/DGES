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
import com.tranetech.dges.R;
import com.tranetech.dges.adapters.ResultAdapter;
import com.tranetech.dges.seter_geter.ResultData;
import com.tranetech.dges.utils.ErrorAlert;
import com.tranetech.dges.utils.GetIP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityResult extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private List<ResultData> resultDatas = new ArrayList<>();
    private android.support.v7.widget.RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ResultAdapter resultAdapter;
    private TextView txtEmpty;
    private String StudentId;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Result");
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sr_result);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_result);
        txtEmpty = (TextView) findViewById(R.id.txt_result_empty);
        mIntent = getIntent();
        StudentId = mIntent.getStringExtra("sid");
    }

    @Override
    public void onRefresh() {
        if (resultAdapter != null) {
            resultAdapter.clear();
            getData(StudentId);
            resultAdapter.addALL(resultDatas);
            swipeRefreshLayout.setRefreshing(false);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            ErrorAlert.error("No data available", ActivityResult.this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(StudentId);
    }

    private void getData(final String StudentId) {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading data...", "Please wait...", false, false);
        GetIP getIP = new GetIP();
        String strUrl = getIP.updateip("test.php");
        StringRequest postRequest = new StringRequest(Request.Method.POST, strUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response StudentAll data : ", response);
                        if (resultAdapter != null) {
                            resultAdapter.clear();
                        }
                        loading.dismiss();
                        try {
                            getJson(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();

                            IntialAdapter();

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
                        ErrorAlert.error(message, ActivityResult.this);
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

    private void getJson(String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);
        String res = jsonObject.getString("list");
        if(res.equals("0")){
//            ErrorAlert.error("No Data Available", ActivityResult.this);
            swipeRefreshLayout.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
        }
        else {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                ResultData ResultData = new ResultData();
                JSONObject jobj = jsonArray.getJSONObject(i);
                ResultData.setTestType(jobj.getString("testType"));
                ResultData.setDate(jobj.getString("date"));
                ResultData.setsResultSub(jobj.getString("subject"));
                ResultData.setsResultObtMarks(jobj.getString("obtainMarks"));
                ResultData.setsResultOutMarks(jobj.getString("totalMarks"));

                resultDatas.add(ResultData);
            }
        }

        IntialAdapter();
    }

    public void IntialAdapter() {
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ActivityResult.this);
        recyclerView.setLayoutManager(mLayoutManager);
        resultAdapter = new ResultAdapter(resultDatas, this);
//        recyclerView.scrollToPosition(resultDatas.size() + 1);
//        resultAdapter.notifyItemInserted(resultDatas.size() + 1);
        recyclerView.setAdapter(resultAdapter);
//        if(resultDatas.size()==0){
//            recyclerView.setVisibility(View.GONE);
//            txtEmpty.setVisibility(View.VISIBLE);
//        }else {
//            recyclerView.setVisibility(View.VISIBLE);
//            txtEmpty.setVisibility(View.GONE);
//        }
    }

}





