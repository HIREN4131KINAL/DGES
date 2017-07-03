package com.tranetech.dges.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.tranetech.dges.R;
import com.tranetech.dges.seter_geter.GetAllData;
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
    Animation startAnimation;
    TextView txt_sname;
    ImageView img_student_profile;
    private SharedPreferenceManager preferenceManager;
    private String StudentId, stu_name, stu_photo, std_ID, strTopic;
    private List<GetAllData> StudentInfoDataList = new ArrayList<>();
    // private FileCacher<List<GetAllData>> stringCachStuInfo = new FileCacher<>(ActivityMainDashBord.this, "cacheGetAll.txt");
    private GetAllData getAllData;
    private CardView cvCircular, cvResult, cvFees, cvHomework, cvObservation, cvLeave, cvEnquiry, cvPolicy;
    private Intent mIntent, IGetPhoto, IGetName, IstdID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dash_bord);
        preferenceManager = new SharedPreferenceManager();
        txt_sname = (TextView) findViewById(R.id.txt_sname);
        img_student_profile = (ImageView) findViewById(R.id.img_student_profile);
        cvCircular = (CardView) findViewById(R.id.card_circular);
        cvResult = (CardView) findViewById(R.id.card_result);
        cvObservation = (CardView) findViewById(R.id.card_observation);
        cvHomework = (CardView) findViewById(R.id.card_homework);
        cvFees = (CardView) findViewById(R.id.card_fees);
        cvLeave = (CardView) findViewById(R.id.card_leave);
        cvEnquiry = (CardView) findViewById(R.id.card_enquiry);
        cvPolicy = (CardView) findViewById(R.id.card_policies);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Dashboard");

        startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blinking);

        mIntent = getIntent();
        StudentId = mIntent.getStringExtra("stu_id");
        strTopic = mIntent.getStringExtra("topic");
        if (strTopic != null) {
            if (strTopic.equals("observation")) {
                cvObservation.startAnimation(startAnimation);
            } else if (strTopic.equals("leave")) {
                cvLeave.startAnimation(startAnimation);
            } else if (strTopic.equals("circular")) {
                cvCircular.startAnimation(startAnimation);
            }
        }
        std_ID = mIntent.getStringExtra("std_ID");

        GetData(StudentId);

//        stu_name = mIntent.getStringExtra("stu_name");
//        Log.e("get name: ", stu_name);

//        stu_photo = mIntent.getStringExtra("photo");
//        Log.e("get photo: ", stu_photo);

        //  txt_sname.setText(stu_name);

        Glide.with(getApplicationContext())
                .load(stu_photo)
                .placeholder(R.drawable.ic_profile)
                .centerCrop()
                .crossFade()
                .into(img_student_profile);
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
                //     stringCachStuInfo.writeCache(StudentInfoDataList);

            }

            stu_name = getAllData.getsName() + " " + getAllData.getmName() + " " + getAllData.getlName();
            txt_sname.setText(stu_name);
            std_ID = getAllData.getsStudentID();
            stu_photo = getAllData.getPhoto();

            Glide
                    .with(getApplicationContext())
                    .load(getAllData.getPhoto())
                    .placeholder(R.drawable.ic_profile)
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
        Result.putExtra("sid", StudentId);
        startActivity(Result);

    }

    public void cardFees(View v) {
        //have to send student id
        Intent Fees = new Intent(this, ActivityFees.class);
        Fees.putExtra("sid", StudentId);
        Fees.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Fees);

    }

    public void LinearProfile(View v) throws IOException {

        Intent Profile = new Intent(this, ActivityProfile.class);
        Profile.putExtra("stdid", StudentId);
        Profile.putExtra("stu_name", stu_name);
        Profile.putExtra("stu_photo", stu_photo);
        Profile.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(Profile);

    }

    public void cardHomework(View v) {
        //have to send standard id
        Intent Homework = new Intent(this, ActivityHomework.class);
        Homework.putExtra("std_ID", std_ID);
        startActivity(Homework);
    }

    public void cardCircular(View v) {
        cvCircular.clearAnimation();
        Intent Circular = new Intent(this, ActivityCircular.class);
        startActivity(Circular);

    }


    public void cardObservation(View v) {
        //have to send student id
        cvObservation.clearAnimation();
        //startAnimation.cancel();
        Intent Updates = new Intent(this, ActivityObservation.class);
        Updates.putExtra("sid", StudentId);
        Updates.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(Updates);

    }

    public void cardPolicies(View v) {

        Intent Policies = new Intent(this, ActivityPolicies.class);
        startActivity(Policies);
    }

    public void cardEnquiry(View v) {
        Intent inq = new Intent(this, Activityinquiry.class);
        inq.putExtra("sid", StudentId);
        inq.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(inq);
    }

    public void cardLeave(View v) {
        cvLeave.clearAnimation();
        Intent Leave = new Intent(this, ActivityLeave.class);
        Leave.putExtra("sid", StudentId);
        Leave.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(Leave);
    }

    public void cardGallery(View v){
        Intent Gallery = new Intent(this, GalleryActivity.class);
        startActivity(Gallery);
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
            case R.id.menu_logout:
                get_ready_logout();
                return true;
            case R.id.menu_change_password:
                Intent in = new Intent(this,ChangePasswordActivity.class);
                startActivity(in);
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
