package com.tranetech.dges.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.tranetech.dges.adapters.LeaveAdapter;
import com.tranetech.dges.seter_geter.LeaveData;
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

public class ActivityLeave extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private List<LeaveData> leaveDatas = new ArrayList<>();
    private FileCacher<List<LeaveData>> fileCacherLeave;
    private RecyclerView recyclerView;
    private String StudentId;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LeaveAdapter LeaveAdapter;
    private Intent mIntent;
    private Button btn_leave;
    private EditText et_leave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sr_leave);
        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.rv_leave);
        et_leave = (EditText) findViewById(R.id.et_leave);

        btn_leave = (Button) findViewById(R.id.btn_leave);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Leave reports");

        mIntent = getIntent();
        StudentId = mIntent.getStringExtra("sid");
        fileCacherLeave = new FileCacher<>(ActivityLeave.this, "stu_leave_" + StudentId + ".txt");
        btn_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(StudentId);
            }
        });

    }

    @Override
    public void onRefresh() {
        if (LeaveAdapter != null) {
            leaveDatas.clear();
            getData(StudentId);
            LeaveAdapter.addALL(leaveDatas);
            swipeRefreshLayout.setRefreshing(false);
        } else {
            ErrorAlert.error("No data available", ActivityLeave.this);
        }
    }

    @Override
    protected void onResume() {

        if (LeaveAdapter != null) {
            leaveDatas.clear();
        }
        getData(StudentId);
        super.onResume();
    }


    private void getData(final String StudentId) {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading data...", "Please wait...", false, false);
        GetIP getIP = new GetIP();
        String strUrl = getIP.updateip("leave.php");
        StringRequest postRequest = new StringRequest(Request.Method.POST, strUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Leave data : ", response + StudentId);
                        loading.dismiss();
                        if (LeaveAdapter != null) {
                            leaveDatas.clear();
                        }
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
                            leaveDatas = fileCacherLeave.readCache();
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
                        ErrorAlert.error(message, ActivityLeave.this);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("sid", StudentId);
                params.put("description", et_leave.getText().toString());
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);
    }


    private void getJson(String response) throws JSONException, IOException {

        JSONObject jsonObject = new JSONObject(response);
        String msg = jsonObject.getString("msg");

        if (msg.equals("1")) {
            Toast.makeText(this, "Send successfully", Toast.LENGTH_SHORT).show();

        }

        JSONArray jsonArray = jsonObject.getJSONArray("list");

        for (int i = 0; i < jsonArray.length(); i++) {

            LeaveData L_Data = new LeaveData();
            JSONObject jobj = jsonArray.getJSONObject(i);

            L_Data.setsLeavelId(jobj.getString("lId"));
            L_Data.setsLeavedescription(jobj.getString("description"));
            L_Data.setsLeavedate(jobj.getString("date"));

            L_Data.setsLeavestatus(jobj.getString("status"));

            leaveDatas.add(L_Data);
            fileCacherLeave.writeCache(leaveDatas);
        }

        IntialAdapter();
    }


    public void IntialAdapter() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // allows for optimizations if all item views are of the same size:
        recyclerView.setHasFixedSize(true);
        LeaveAdapter = new LeaveAdapter(leaveDatas, this);
        recyclerView.setAdapter(LeaveAdapter);

    }

}
