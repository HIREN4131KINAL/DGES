package com.tranetech.dges;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HIREN AMALIYAR on 27-05-2017.
 */

public class ActivityParentsMultiChild extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<ParentChildData> parentChildDataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterParentsMultiChild adapterParentsMultiChild;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_multi_child);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sr_parents_multi_stu);
        recyclerView = (RecyclerView) findViewById(R.id.rv_parents_multi_stu);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void onRefresh() {
        getData();
        swipeRefreshLayout.setRefreshing(false);
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
                ErrorAlert.error(message, ActivityParentsMultiChild.this);
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getJson(String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        for (int i = 0; i < jsonArray.length(); i++) {
            ParentChildData parentChildData = new ParentChildData();
            JSONObject jobj = jsonArray.getJSONObject(i);
            parentChildData.setsStudentID(jobj.getString("uid"));
            parentChildData.setsName(jobj.getString("name"));
            parentChildData.setsStandard(jobj.getString("standard"));

            parentChildDataList.add(parentChildData);
        }

        IntialAdapter();
    }

    public void IntialAdapter() {
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ActivityParentsMultiChild.this);
        recyclerView.setLayoutManager(mLayoutManager);
        adapterParentsMultiChild = new AdapterParentsMultiChild(parentChildDataList, getApplicationContext());
        recyclerView.scrollToPosition(parentChildDataList.size() + 1);
        adapterParentsMultiChild.notifyItemInserted(parentChildDataList.size() + 1);
        recyclerView.setAdapter(adapterParentsMultiChild);
    }

}
