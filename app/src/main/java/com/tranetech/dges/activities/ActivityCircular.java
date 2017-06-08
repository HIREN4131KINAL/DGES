package com.tranetech.dges.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
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
import com.tranetech.dges.adapters.CircularAdapter;
import com.tranetech.dges.seter_geter.CircularData;
import com.tranetech.dges.utils.ErrorAlert;
import com.tranetech.dges.utils.GetIP;
import com.tranetech.dges.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityCircular extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private List<CircularData> circularDatas = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CircularAdapter circularAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_circular);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Circular");

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sr_circular);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_circular);
    }

    @Override
    public void onRefresh() {
        if (circularAdapter != null) {
            circularAdapter.clear();
            getData();
            circularAdapter.addALL(circularDatas);
            swipeRefreshLayout.setRefreshing(false);
        } else {
            ErrorAlert.error("No data available", ActivityCircular.this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (circularAdapter != null) {
            circularAdapter.clear();
        }
        getData();
    }

    private void getData() {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading data...", "Please wait...", false, false);

        RequestQueue queue = Volley.newRequestQueue(this);
        GetIP getIP = new GetIP();
        String url = getIP.updateip("circular.php");

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
                ErrorAlert.error(message, ActivityCircular.this);
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getJson(String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        for (int i = 0; i < jsonArray.length(); i++) {
            CircularData circularData = new CircularData();
            JSONObject jobj = jsonArray.getJSONObject(i);
            circularData.setsCircularTitle(jobj.getString("title"));
            circularData.setsCircularDesc(jobj.getString("description"));
            circularData.setsCircualarDate(jobj.getString("date"));
            circularData.setsCircualarStatus(jobj.getString("status"));
            String url = jobj.getString("cfile");
            if(!url.equals("null"))
            {
                circularData.setsCircularURL(url);
                circularData.setsCircularFileName(jobj.getString("filename"));
            }
            circularDatas.add(circularData);
        }

        IntialAdapter();
    }

    public void IntialAdapter() {
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ActivityCircular.this);
        recyclerView.setLayoutManager(mLayoutManager);
        circularAdapter = new CircularAdapter(circularDatas, this);
       /* recyclerView.scrollToPosition(circularDatas.size() + 1);
        circularAdapter.notifyItemInserted(circularDatas.size() + 1);*/
        recyclerView.setAdapter(circularAdapter);
    }

}
