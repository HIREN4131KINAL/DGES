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
import com.tranetech.dges.seter_geter.ParentChildData;
import com.tranetech.dges.utils.ErrorAlert;
import com.tranetech.dges.utils.GetIP;
import com.tranetech.dges.adapters.HomeworkAdapter;
import com.tranetech.dges.seter_geter.HomeworkData;
import com.tranetech.dges.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivityHomework extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private List<HomeworkData> hwData = new ArrayList<>();
    private List<ParentChildData> parentChildDataList;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private HomeworkAdapter hwAdapter;
    private int intValue;
    ParentChildData parentChildData;
    private FileCacher<List<ParentChildData>> stringCacherList = new FileCacher<>(ActivityHomework.this, "cacheListTmp.txt");
    String Student_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Homework");


        Intent mIntent = getIntent();
        intValue = mIntent.getIntExtra("position", 0);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sr_homework);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_homework);

        try {
            parentChildDataList = stringCacherList.readCache();
        } catch (IOException e) {
            e.printStackTrace();
        }

        parentChildData = parentChildDataList.get(intValue);

        Student_ID = parentChildData.getsStudentID();
        Log.e("onCreate: ", Student_ID);
    }

    @Override
    public void onRefresh() {
        getData();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading Data", "Please wait...", false, false);

        RequestQueue queue = Volley.newRequestQueue(this);
        GetIP getIP = new GetIP();
        String url = getIP.updateip("emp_checkin_log.php");

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        swipeRefreshLayout.setRefreshing(false);
                        try {
                            getJson(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                loading.dismiss();
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
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
                ErrorAlert.error(message, ActivityHomework.this);
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getJson(String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        for (int i = 0; i < jsonArray.length(); i++) {
            HomeworkData homeworkData = new HomeworkData();
            JSONObject jobj = jsonArray.getJSONObject(i);
            homeworkData.setsSubName(jobj.getString("str_gr_no"));
            homeworkData.setsHWDate(jobj.getString("name"));
            homeworkData.setsHWDescription(jobj.getString("address"));

            hwData.add(homeworkData);
        }

        IntialAdapter();
    }

    public void IntialAdapter() {
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ActivityHomework.this);
        recyclerView.setLayoutManager(mLayoutManager);
        hwAdapter = new HomeworkAdapter(hwData, this);
        recyclerView.scrollToPosition(hwData.size() + 1);
        hwAdapter.notifyItemInserted(hwData.size() + 1);
        recyclerView.setAdapter(hwAdapter);
    }
}
