package com.tranetech.dges.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
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
import com.tranetech.dges.FirebaseServices.Config;
import com.tranetech.dges.FirebaseServices.NotificationUtils;
import com.tranetech.dges.R;
import com.tranetech.dges.adapters.ObservationAdapter;
import com.tranetech.dges.seter_geter.ObservationData;
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

public class ActivityObservation extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private List<ObservationData> observationDatas = new ArrayList<>();
    private FileCacher<List<ObservationData>> str_CachList_observation;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ObservationAdapter observationAdapter;
    private String studentID;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Observation");

        Intent mIntent = getIntent();
        studentID = mIntent.getStringExtra("sid");

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sr_observation);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_observation);
        txtEmpty = (TextView) findViewById(R.id.txt_observation_empty);
        str_CachList_observation = new FileCacher<>(ActivityObservation.this, "stu_observation_" + studentID + ".txt");
        txtEmpty = (TextView) findViewById(R.id.txt_observation_empty);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                swipeRefreshLayout.setOnRefreshListener(ActivityObservation.this);
                String message = intent.getStringExtra("message");
                if (message.equals("leave")) {
                    observationAdapter.clear();
                    GetData(studentID);
                    observationAdapter.addALL(observationDatas);
                }
            }
        };
    }

    @Override
    public void onRefresh() {
        if (observationAdapter != null) {
            observationAdapter.clear();
            GetData(studentID);
            observationAdapter.addALL(observationDatas);
            swipeRefreshLayout.setRefreshing(false);
        } else {
            ErrorAlert.error("No data available for this student.", ActivityObservation.this);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register new push message receiver
// by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

// clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
        if (observationAdapter != null) {
            observationAdapter.clear();
        }
        GetData(studentID);
        super.onResume();
    }

    public void GetData(final String studentID) {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading data...", "Please wait...", false, false);
        GetIP getIP = new GetIP();
        String strUrl = getIP.updateip("observation.php");
        StringRequest postRequest = new StringRequest(Request.Method.POST, strUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response Observation : ", response);
                        swipeRefreshLayout.setRefreshing(false);

                        if (observationAdapter != null) {
                            observationAdapter.clear();
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
                        swipeRefreshLayout.setRefreshing(false);
                        loading.dismiss();

                        try {
                            if (str_CachList_observation.hasCache()) {
                                str_CachList_observation.readCache();
                            }

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
                        ErrorAlert.error(message, ActivityObservation.this);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("sid", studentID);
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
                ObservationData observationData = new ObservationData();
                JSONObject jobj = jsonArray.getJSONObject(i);

                String name = jobj.getString("fName") + " " + jobj.getString("lName");

                observationData.setsObStudentName(jobj.getString("teacher"));
                observationData.setsObDate(jobj.getString("date"));
                observationData.setsObTitle(jobj.getString("subject"));
                observationData.setsObDesc(jobj.getString("remarks"));

                observationDatas.add(observationData);
                str_CachList_observation.writeCache(observationDatas);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                txtEmpty.setVisibility(View.GONE);
            }
        }
        IntialAdapter();
    }

    public void IntialAdapter() {
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ActivityObservation.this);
        recyclerView.setLayoutManager(mLayoutManager);
        observationAdapter = new ObservationAdapter(observationDatas, this);
        /*recyclerView.scrollToPosition(observationDatas.size() + 1);
        observationAdapter.notifyItemInserted(observationDatas.size() + 1);*/
        recyclerView.setAdapter(observationAdapter);
    }
}
