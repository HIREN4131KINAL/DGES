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
import com.tranetech.dges.R;
import com.tranetech.dges.adapters.HomeworkAdapter;
import com.tranetech.dges.seter_geter.HomeworkData;
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

public class ActivityHomework extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private List<HomeworkData> hwData = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private HomeworkAdapter hwAdapter;
    private FileCacher<List<HomeworkData>> stringCacherHomeworkList = new FileCacher<>(ActivityHomework.this, "cacheListTmp.txt");
    private HomeworkData homeworkData;
    private String standardID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Homework");


        Intent mIntent = getIntent();
        standardID = mIntent.getStringExtra("standard");

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sr_homework);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_homework);

        Log.e("onCreate: Standrd id ", standardID);

    }

    @Override
    public void onRefresh() {
        if (hwAdapter != null) {
            hwAdapter.clear();
            GetData(standardID);
            hwAdapter.addALL(hwData);
            swipeRefreshLayout.setRefreshing(false);
        } else {
            ErrorAlert.error("No data available,please add employee data from employee list", ActivityHomework.this);
        }

    }

    @Override
    protected void onResume() {

        if (hwAdapter != null) {
            hwAdapter.clear();
        }
        GetData(standardID);
        super.onResume();
    }

    public void GetData(final String standardID) {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading data...", "Please wait...", false, false);
        GetIP getIP = new GetIP();
        String strUrl = getIP.updateip("homework.php");
        StringRequest postRequest = new StringRequest(Request.Method.POST, strUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response Homework : ", response);
                        swipeRefreshLayout.setRefreshing(false);
                        loading.dismiss();
                        try {
                            getjson(response);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        swipeRefreshLayout.setRefreshing(false);
                        loading.dismiss();

                        try {
                            hwData = stringCacherHomeworkList.readCache();
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
                        ErrorAlert.error(message, ActivityHomework.this);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("stdid", standardID);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);
    }

    public void getjson(String response) throws IOException {
        JSONObject jsonObject1 = null;
        // store response in cache memory

        try {
            jsonObject1 = new JSONObject(response);
            JSONArray jsonArray = jsonObject1.getJSONArray("list");


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jobj = null;
                jobj = jsonArray.getJSONObject(i);
                homeworkData = new HomeworkData();
                homeworkData.setsHWDate(jobj.getString("date"));
                homeworkData.setsHWDescription(jobj.getString("containt"));
                homeworkData.setsSubName(jobj.getString("subject"));
                homeworkData.setStandrdID(jobj.getString("hId"));
                hwData.add(homeworkData);
                stringCacherHomeworkList.writeCache(hwData);
            }
        } catch (JSONException e) {
            System.out.print(e.toString());
        }
        IntialHomwrkAdapter();
    }

    private void IntialHomwrkAdapter() {
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ActivityHomework.this);
        recyclerView.setLayoutManager(mLayoutManager);
        hwAdapter = new HomeworkAdapter(hwData, this);

      /*  recyclerView.scrollToPosition(hwData.size() + 1);
        hwAdapter.notifyItemInserted(hwData.size() + 1);*/
        recyclerView.setAdapter(hwAdapter);

    }


}
