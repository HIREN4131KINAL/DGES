package com.tranetech.dges.activities;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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
import com.google.firebase.messaging.FirebaseMessaging;
import com.kosalgeek.android.caching.FileCacher;
import com.rampo.updatechecker.UpdateChecker;
import com.tranetech.dges.FirebaseServices.Config;
import com.tranetech.dges.R;
import com.tranetech.dges.adapters.AdapterParentsMultiChild;
import com.tranetech.dges.seter_geter.ParentChildData;
import com.tranetech.dges.utils.ErrorAlert;
import com.tranetech.dges.utils.GetIP;
import com.tranetech.dges.utils.SharedPreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HIREN AMALIYAR on 27-05-2017.
 */

public class ActivityParentsMultiChild extends AppCompatActivity {
    private static final String TAG = ActivityParentsMultiChild.class.getSimpleName();
    private SharedPreferenceManager preferenceManager;
    private List<ParentChildData> parentChildDataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterParentsMultiChild adapterParentsMultiChild;
    private FileCacher<List<ParentChildData>> stringCachParentChild = new FileCacher<>(ActivityParentsMultiChild.this, "cacheListTmp.txt");
    private String MobileNo, topic;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_multi_child);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("DGES");

        recyclerView = (RecyclerView) findViewById(R.id.rv_parents_multi_stu);
        preferenceManager = new SharedPreferenceManager();

        MobileNo = preferenceManager.getDefaults("mobile", getApplicationContext());

        if (MobileNo == null) {
            Intent getMobile = getIntent();
            MobileNo = getMobile.getStringExtra("mobile");
            preferenceManager.setDefaults("mobile", MobileNo, getApplicationContext());
        }

        Intent in = getIntent();
        topic = in.getStringExtra("topic");

        Log.e("MobileNo: ", MobileNo);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    updateFirebaseRegId();

                }
            }
        };

        updateFirebaseRegId();
        UpdateChecker checker = new UpdateChecker(this); // If you are in a Activity or a FragmentActivity
        checker.start();

    }

    private void updateFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId)) {
            updateDeviceKey(regId);
        } else {
        }
    }

    private void updateDeviceKey(final String regid) {
        final ProgressDialog loading = ProgressDialog.show(this, "Updating Device Id", "Please wait...", false, false);
        GetIP getIP = new GetIP();
        String url = getIP.updateip("update_device_key.php");

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        try {
                            updateKey(response);
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", MobileNo);
                params.put("key1", regid);
                return params;
            }
        };
// Add the request to the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private void updateKey(String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);

        String msg = jsonObject.getString("msg");
        Log.e(TAG, msg);
    }


    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        if (adapterParentsMultiChild != null) {
            adapterParentsMultiChild.clear();
        }

        GetData(MobileNo);

        if (adapterParentsMultiChild != null) {
            adapterParentsMultiChild.clear();
            adapterParentsMultiChild.addALL(parentChildDataList);
        }


        super.onResume();
    }

    public void GetData(final String MobileNo) {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading data...", "Please wait...", false, false);
        GetIP getIP = new GetIP();
        String strUrl = getIP.updateip("load_multichild.php");
        StringRequest postRequest = new StringRequest(Request.Method.POST, strUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response Homework : ", response);

                        if (adapterParentsMultiChild != null) {
                            adapterParentsMultiChild.clear();
                        }

                        loading.dismiss();
                        try {
                            getjson(response);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();


                        try {
                            parentChildDataList = stringCachParentChild.readCache();
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
                        ErrorAlert.error(message, ActivityParentsMultiChild.this);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", MobileNo);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);
    }

    public void getjson(String response) throws JSONException, IOException {
        Log.e("get json data : ", response);
        try {
            JSONObject jsonObject = new JSONObject(response);

            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                ParentChildData parentChildData = new ParentChildData();

                JSONObject jobj = jsonArray.getJSONObject(i);

                parentChildData.setsStudentID(jobj.getString("sId"));
                parentChildData.setsName(jobj.getString("fName"));
                parentChildData.setmName(jobj.getString("mName"));
                parentChildData.setlName(jobj.getString("lName"));
                parentChildData.setsStandard(jobj.getString("std"));
                parentChildData.setPhoto(jobj.getString("photo"));
                parentChildData.setsStandard_ID(jobj.getString("stdId"));
                parentChildData.setTopic(topic);

                parentChildDataList.add(parentChildData);
                stringCachParentChild.writeCache(parentChildDataList);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        IntialAdapter();
    }

    public void IntialAdapter() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ActivityParentsMultiChild.this);
        recyclerView.setLayoutManager(mLayoutManager);
        adapterParentsMultiChild = new AdapterParentsMultiChild(parentChildDataList, getApplicationContext());
       /* recyclerView.scrollToPosition(parentChildDataList.size() + 1);
        adapterParentsMultiChild.notifyItemInserted(parentChildDataList.size() + 1);*/
        recyclerView.setAdapter(adapterParentsMultiChild);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_emp_logout:
                get_ready_logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void get_ready_logout() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Would you like to logout?");
        builder.setIcon(R.drawable.logo_main);
        builder.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        preferenceManager = new SharedPreferenceManager();
                        preferenceManager.ClearAllPreferences(getApplicationContext());

                        ActivityLogin.settings.edit().clear().apply();

                        Intent toLoginActivity = new Intent(ActivityParentsMultiChild.this, ActivityLogin.class);
                        toLoginActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(toLoginActivity);
                        finish();
                    }
                });

        // display dialog
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // user doesn't want to logout
            }
        });


        android.support.v7.app.AlertDialog dialog = builder.create();

        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
