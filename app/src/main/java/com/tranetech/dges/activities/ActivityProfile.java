package com.tranetech.dges.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
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
import com.tranetech.dges.seter_geter.GetAllData;
import com.tranetech.dges.seter_geter.ParentChildData;
import com.tranetech.dges.R;
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

public class ActivityProfile extends AppCompatActivity {

    private TextView txtSname, txtClass, txtRollNo, txtGrNo, txtBday, txtGender, txtPhone, txtAddress, txt_adhar, txt_blood, txt_status;
    private ImageView imgProfile;
    private List<GetAllData> StudentInfoDataList = new ArrayList<>();
    //private FileCacher<List<ParentChildData>> stringCacherList = new FileCacher<>(ActivityProfile.this, "cacheListTmp.txt");
    private GetAllData getAllData;
    private static final String PREFS_NAME = "ActivityProfile";
    private String StudentId, stu_name, stu_photo;
    private Intent mIntent, IGetName, IGetPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("Profile");
        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mIntent = getIntent();
        StudentId = mIntent.getStringExtra("stdid");

        GetData(StudentId);

        IGetName = getIntent();
        stu_name = IGetName.getStringExtra("stu_name");
        Log.e("get name: ", stu_name);

        IGetPhoto = getIntent();
        stu_photo = IGetPhoto.getStringExtra("stu_photo");
        Log.e("get photo: ", stu_photo);


        LoaduiElements();

        txtSname.setText(stu_name);

        Glide.with(getApplicationContext())
                .load(stu_photo)
                .placeholder(R.drawable.ic_profile)
                .centerCrop()
                .crossFade()
                .into(imgProfile);


    }

    private void SetAlldata() {

        txtClass.setText(getAllData.getsStandard() + "(" + getAllData.getDivision() + ")");
        txtRollNo.setText(getAllData.getRollno());
        txtGrNo.setText(getAllData.getGrNo());
        txtBday.setText(getAllData.getDob());
        txtGender.setText(getAllData.getGender());
        txtPhone.setText(getAllData.getMobile());
        txtAddress.setText(getAllData.getAddress());

        txt_adhar.setText(getAllData.getAdhar());
        txt_blood.setText(getAllData.getBloodGroup());
        txt_status.setText(getAllData.getStatus());


    }


    private void LoaduiElements() {
        txtSname = (TextView) findViewById(R.id.txt_sname);
        txtClass = (TextView) findViewById(R.id.txt_class);
        txtRollNo = (TextView) findViewById(R.id.txt_roll);
        txtGrNo = (TextView) findViewById(R.id.txt_grno);
        txtBday = (TextView) findViewById(R.id.txt_bday);
        txtGender = (TextView) findViewById(R.id.txt_gen);
        txtPhone = (TextView) findViewById(R.id.txt_phno);
        txtAddress = (TextView) findViewById(R.id.txt_add);

        txt_adhar = (TextView) findViewById(R.id.txt_adhar);
        txt_blood = (TextView) findViewById(R.id.txt_blood);
        txt_status = (TextView) findViewById(R.id.txt_status);
        imgProfile = (ImageView) findViewById(R.id.img_student_profile);
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
                        ErrorAlert.error(message, ActivityProfile.this);
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

                getAllData.setRollno(jobj.getString("rollNo"));
                getAllData.setGrNo(jobj.getString("grNo"));
                getAllData.setDob(jobj.getString("dob"));
                getAllData.setGender(jobj.getString("gender"));
                getAllData.setMobile(jobj.getString("mobile"));
                getAllData.setAddress(jobj.getString("address"));


                getAllData.setAdhar(jobj.getString("adhar"));
                getAllData.setDivision(jobj.getString("div"));
                getAllData.setBloodGroup(jobj.getString("bloodgroup"));
                getAllData.setStatus(jobj.getString("status"));


                StudentInfoDataList.add(getAllData);


                Log.e("getjson in for: ", jobj.getString("fName"));


                // stringCacherList.writeCache(StudentInfoDataList);
                //Store_Object_of_GetAllData.writeCache(getAllData);
            }

            SetAlldata();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
