package com.tranetech.dges.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.kosalgeek.android.caching.FileCacher;
import com.tranetech.dges.R;
import com.tranetech.dges.seter_geter.GetAllData;
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

public class ActivityMainDashBord extends AppCompatActivity {
    private SharedPreferenceManager preferenceManager;
    public String StudentId;
    private List<GetAllData> StudentInfoDataList = new ArrayList<>();
    private GetAllData getAllData;
    private Intent mIntent;
    TextView txt_sname;
    ImageView img_student_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dash_bord);
        preferenceManager = new SharedPreferenceManager();
        txt_sname = (TextView) findViewById(R.id.txt_sname);
        img_student_profile = (ImageView) findViewById(R.id.img_student_profile);

        //   stringCacherList = new FileCacher<>(ActivityMainDashBord.this, "cacheListTmp.txt");
        //  Store_Object_of_GetAllData = new FileCacher<>(ActivityMainDashBord.this, "SorageOFobj.txt");
        // get action bar


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Dash Board");


        if (StudentId == null) {
            mIntent = getIntent();
            StudentId = mIntent.getStringExtra("stdid");
        }

        GetData(StudentId);
    }


    private void GetData(final String StudentId) {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading data...", "Please wait...", false, false);
        GetIP getIP = new GetIP();
        String strUrl = getIP.updateip("get_student_info.php");
        StringRequest postRequest = new StringRequest(Request.Method.POST, strUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response StudentAll data : ", response);
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

                     /*   try {
                            getAllData = Store_Object_of_GetAllData.readCache();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
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
                        ErrorAlert.error(message, ActivityMainDashBord.this);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("stdid", StudentId);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);
    }

    //
    private void getjson(String response) throws JSONException, IOException {
        Log.e("get json data : ", response);
        try {
            JSONObject jsonObject = new JSONObject(response);

            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {

                getAllData = new GetAllData();

                JSONObject jobj = jsonArray.getJSONObject(i);

                getAllData.setsStudentID(jobj.getString("sId"));
                getAllData.setsName(jobj.getString("fName"));
                getAllData.setmName(jobj.getString("mName"));
                getAllData.setlName(jobj.getString("lName"));
                getAllData.setsStandard(jobj.getString("std"));
                getAllData.setsStandard_ID(jobj.getString("stdid"));
                getAllData.setPhoto(jobj.getString("photo"));

                StudentInfoDataList.add(getAllData);
                // stringCacherList.writeCache(StudentInfoDataList);

                //Store_Object_of_GetAllData.writeCache(getAllData);
            }


            txt_sname.setText(getAllData.getsName() + " " + getAllData.getmName() + " " + getAllData.getlName());

            Glide
                    .with(getApplicationContext())
                    .load(getAllData.getPhoto())
                    .centerCrop()
                    .crossFade()
                    .into(img_student_profile);


        } catch (JSONException e) {
            e.printStackTrace();
        }/* catch (IOException e) {
            e.printStackTrace();
        }*/

    }


    public void cardResult(View v) {

        Intent Result = new Intent(this, ActivityResult.class);
        startActivity(Result);

    }

    public void cardFees(View v) {
//have to send student id
        Intent Fees = new Intent(this, ActivityFees.class);
        startActivity(Fees);

    }

    public void LinearProfile(View v) throws IOException {

        Intent Profile = new Intent(this, ActivityProfile.class);
        Profile.putExtra("stdid", StudentId);
        Profile.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(Profile);

    }

    public void cardHomework(View v) {
        //have to send standard id
        Intent Homework = new Intent(this, ActivityHomework.class);
        Homework.putExtra("standard", getAllData.getsStandard_ID());
        startActivity(Homework);
    }

    public void cardCircular(View v) {

        Intent Circular = new Intent(this, ActivityCircular.class);
        startActivity(Circular);

    }


    public void cardObservation(View v) {
        //have to send student id
        Intent Updates = new Intent(this, ActivityObservation.class);
        startActivity(Updates);

    }

    public void cardPolicies(View v) {

        Intent Policies = new Intent(this, ActivityProfile.class);
        startActivity(Policies);
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

                        Intent toLoginActivity = new Intent(ActivityMainDashBord.this, ActivityLogin.class);
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

    @Override
    protected void onDestroy() {
        preferenceManager.setDefaults("stdid", null, getApplicationContext());
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        preferenceManager.setDefaults("stdid", null, getApplicationContext());
        super.onPause();
    }
}
