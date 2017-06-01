package com.tranetech.dges.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.tranetech.dges.seter_geter.ParentChildData;
import com.tranetech.dges.utils.ErrorAlert;
import com.tranetech.dges.utils.GetIP;
import com.tranetech.dges.adapters.HomeworkAdapter;
import com.tranetech.dges.seter_geter.HomeworkData;
import com.tranetech.dges.R;
import com.tranetech.dges.utils.SharedPreferenceManager;

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

    String standard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Homework");


        Intent mIntent = getIntent();
        standard = mIntent.getStringExtra("standard");

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sr_homework);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_homework);

        Log.e("onCreate: ", standard);
        Toast.makeText(this, "standard id is " + standard, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRefresh() {
        GetData();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetData();
    }

    public void GetData() {
        final ProgressDialog loading = ProgressDialog.show(this, "Home work", "Please wait...", false, false);
        GetIP getIP = new GetIP();
        String strUrl = getIP.updateip("login.php");
        StringRequest postRequest = new StringRequest(Request.Method.POST, strUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        loading.dismiss();
                        try {
                            getjson(response);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();
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
                        ErrorAlert.error(message, ActivityLogin.this);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //  params.put("grno", GR_Number);
                params.put("mobile", mobile);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);
    }

    protected void getjson(String response) throws IOException {
        JSONObject jsonObject1 = null;
        // store response in cache memory
        stringCacher.writeCache(response);

        try {
            jsonObject1 = new JSONObject(response);
            JSONArray jsonArray = jsonObject1.getJSONArray("list");
            Log.e("getjson:Response ", response);


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jobj = null;
                jobj = jsonArray.getJSONObject(i);

                msg = jobj.getString("msg");

                Log.e("getjson: ", msg);

                if (msg.equals("0")) {

                    Toast.makeText(this, "Error...", Toast.LENGTH_SHORT).show();
                    Snackbar.make(getCurrentFocus(), "Something Is Wrong, please try again", Snackbar.LENGTH_SHORT).show();
                } else {
                    SharedPreferenceManager.setDefaults_boolean("hasLoggedIn", true, getApplicationContext());
                    //str_gr_no = jobj.getString("grNo");
                    mobile_s = jobj.getString("mobile");


                    Intent intent = new Intent(this, ActivityParentsMultiChild.class);
                    intent.putExtra("response", response);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

                SharedPreferenceManager.setDefaults("msg", msg, getApplicationContext());
                // SharedPreferenceManager.setDefaults("str_gr_no", str_gr_no, getApplicationContext());
                SharedPreferenceManager.setDefaults("mobile_s", mobile_s, getApplicationContext());

                SharedPreferences settings = getSharedPreferences(ActivityLogin.PREFS_NAME, 0); // 0 - for private mode
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("msg", msg);
                //editor.putString("str_gr_no", str_gr_no);
                editor.putString("mobile", mobile_s);
                editor.putBoolean("hasLoggedIn", true);
                editor.apply();

            }
        } catch (JSONException e) {
            System.out.print(e.toString());
        }


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
