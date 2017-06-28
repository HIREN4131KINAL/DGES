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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import com.tranetech.dges.adapters.InquiryAdapter;
import com.tranetech.dges.seter_geter.InquiryData;
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

public class Activityinquiry extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private List<InquiryData> inquiryDatas = new ArrayList<>();
    private FileCacher<List<InquiryData>> fileCacherInquiry;
    private RecyclerView recyclerView;
    private String StudentId;
    private SwipeRefreshLayout swipeRefreshLayout;
    private InquiryAdapter inquiryAdapter;
    private Intent mIntent;
    private Button btn_inquiry;
    private String REsponse;
    private EditText et_inquiry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sr_inquiry);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_inquiry);
        et_inquiry = (EditText) findViewById(R.id.et_inquiry);

        btn_inquiry = (Button) findViewById(R.id.btn_inquiry);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Inquiry");

        mIntent = getIntent();
        StudentId = mIntent.getStringExtra("sid");

        fileCacherInquiry = new FileCacher<>(Activityinquiry.this, "stu_inquiry_" + StudentId + ".txt");

        btn_inquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_inquiry.getText().toString().length() == 0) {
                    et_inquiry.setError("Enter Enquiry.");
                } else {
                    onRefresh();
                }
            }
        });

    }

    @Override
    public void onRefresh() {
        if (inquiryAdapter != null) {
            inquiryDatas.clear();
            getData(StudentId);
            inquiryAdapter.addALL(inquiryDatas);
            swipeRefreshLayout.setRefreshing(false);
        } else {
            ErrorAlert.error("No data available", Activityinquiry.this);
        }
    }

    @Override
    protected void onResume() {

        if (inquiryAdapter != null) {
            inquiryDatas.clear();
        }
        getData(StudentId);
        super.onResume();
    }

    private void getData(final String StudentId) {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading data...", "Please wait...", false, false);
        GetIP getIP = new GetIP();
        String strUrl = getIP.updateip("enquiry.php");
        StringRequest postRequest = new StringRequest(Request.Method.POST, strUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Inquery data : ", response + StudentId);
                        if (inquiryAdapter != null) {
                            inquiryDatas.clear();
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
                            inquiryDatas = fileCacherInquiry.readCache();
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
                        ErrorAlert.error(message, Activityinquiry.this);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("sid", StudentId);
                params.put("enquiry", et_inquiry.getText().toString());
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);
    }


    private void getJson(String response) throws JSONException, IOException {
        try {

            JSONObject jsonObject = new JSONObject(response);

            REsponse = jsonObject.getString("msg");

            if (REsponse.equals("1")) {
                Toast.makeText(this, "Send successfully", Toast.LENGTH_SHORT).show();
            }

            Log.e("Response from server : ", REsponse);


            JSONObject jsonData = new JSONObject(response);
            JSONArray jsonArray = jsonData.getJSONArray("list");


            for (int i = 0; i < jsonArray.length(); i++) {

                InquiryData Inq_Data = new InquiryData();
                JSONObject jobj = jsonArray.getJSONObject(i);

                Inq_Data.setsInquiryeId(jobj.getString("eId"));
                Inq_Data.setsInquiryenquiry(jobj.getString("enquiry"));
                Inq_Data.setsInquirydate(jobj.getString("date"));

                inquiryDatas.add(Inq_Data);
                fileCacherInquiry.writeCache(inquiryDatas);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        IntialAdapter();
    }


    public void IntialAdapter() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
      /*  layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);*/
        recyclerView.setLayoutManager(layoutManager);

        // allows for optimizations if all item views are of the same size:
        recyclerView.setHasFixedSize(false);
        inquiryAdapter = new InquiryAdapter(inquiryDatas, this);
        recyclerView.setAdapter(inquiryAdapter);

    }

}



