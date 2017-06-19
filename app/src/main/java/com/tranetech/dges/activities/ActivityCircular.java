package com.tranetech.dges.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import com.tranetech.dges.adapters.CircularAdapter;
import com.tranetech.dges.seter_geter.CircularData;
import com.tranetech.dges.utils.ErrorAlert;
import com.tranetech.dges.utils.GetIP;
import com.tranetech.dges.R;
import com.tranetech.dges.utils.MarshmallowPermissions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivityCircular extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private List<CircularData> circularDatas = new ArrayList<>();
    private MarshmallowPermissions marsh;

    View parentLayout;
    private static final int REQUEST_PERMISSION = 1;

    private FileCacher<List<CircularData>> circulerCatch = new FileCacher<>(ActivityCircular.this, "cirulerList.txt");

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CircularAdapter circularAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_circular);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Circular");

        marsh = new MarshmallowPermissions(ActivityCircular.this);
        parentLayout = findViewById(android.R.id.content);

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


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(final int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            // for each permission check if the user grantet/denied them
            // you may want to group the rationale in a single dialog,
            // this is just an tranetech
            for (int i = 0, len = permissions.length; i < len; i++) {
                final String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        // user denied flagging NEVER ASK AGAIN
                        // you can either enable some fall back,
                        // disable features of your app
                        // or open another dialog explaining
                        // again the permission and directing to
                        // the app setting
                        marsh.AllowedManually(parentLayout);
                    } else if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(permission) || Manifest.permission.READ_EXTERNAL_STORAGE.equals(permission)) {
                        // showRationale(permission, R.string.permission_denied);
                        // user denied WITHOUT never ask again
                        // this is a good place to explain the user
                        // why you need the permission and ask if he want
                        // to accept it (the rationale)
                        marsh.AllowedManually(parentLayout);
                    }

                } else {
                    if (marsh.checkIfAlreadyhavePermission()) {
                        Toast.makeText(this, "Permission granted for storage", Toast.LENGTH_SHORT).show();
                    } else {
                        marsh.AllowedManually(parentLayout);
                    }
                }
            }
        }

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
                        if (circularAdapter != null) {
                            circularAdapter.clear();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        try {
                            getJson(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                loading.dismiss();

                //getting offline data...
                try {
                    if (circulerCatch.hasCache()) {
                        circularDatas = circulerCatch.readCache();
                        IntialAdapter();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


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

    private void getJson(String response) throws JSONException, IOException {

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
            if (!url.equals("null")) {
                circularData.setsCircularURL(url);
                circularData.setsCircularFileName(jobj.getString("filename"));
            }
            circularDatas.add(circularData);
            circulerCatch.writeCache(circularDatas);
        }

        IntialAdapter();
    }

    public void IntialAdapter() {
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ActivityCircular.this);
        recyclerView.setLayoutManager(mLayoutManager);
        circularAdapter = new CircularAdapter(circularDatas, this, marsh);
       /* recyclerView.scrollToPosition(circularDatas.size() + 1);
        circularAdapter.notifyItemInserted(circularDatas.size() + 1);*/
        recyclerView.setAdapter(circularAdapter);
    }

}
