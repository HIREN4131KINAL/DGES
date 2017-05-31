package com.tranetech.dges.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.kosalgeek.android.caching.FileCacher;
import com.tranetech.dges.seter_geter.ParentChildData;
import com.tranetech.dges.R;

import java.util.List;

public class ActivityProfile extends AppCompatActivity {

    private TextView txtSname, txtClass, txtRollNo, txtGrNo, txtBday, txtGender, txtPhone, txtAddress;
    private ImageView imgProfile;
    private List<ParentChildData> parentChildDataList;
    private FileCacher<List<ParentChildData>> stringCacherList = new FileCacher<>(ActivityProfile.this, "cacheListTmp.txt");
    private ParentChildData parentChildData;
    public static String fullname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("Profile");

        Intent mIntent = getIntent();
        fullname = mIntent.getStringExtra("name");

        //  parentChildData = parentChildDataList.get(fullname);

        LoaduiElements();
        SetAlldata();

    }

    private void SetAlldata() {
        txtSname.setText(fullname);
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
        imgProfile = (ImageView) findViewById(R.id.img_student_profile);
    }








/*
    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    //method for setting student data into textviews
    private void setData() {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading Data", "Please wait...", false, false);

        GetIP getIP = new GetIP();
        String url = getIP.updateip("load_emp_profile.php");

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        loading.dismiss();
                        try {
                            getjson(response);
                        } catch (JSONException e) {
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
                        ErrorAlert.error(message, ActivityProfile.this);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("str_gr_no", uid);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);
    }

    private void getjson(String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jobj = jsonArray.getJSONObject(i);

            txtSname.setText(jobj.getString("sname"));
            txtClass.setText(jobj.getString("class"));
            txtRollNo.setText(jobj.getString("rollno"));
            txtGrNo.setText(jobj.getString("grno"));
            txtBday.setText(jobj.getString("bday"));
            txtGender.setText(jobj.getString("gender"));
            txtPhone.setText(jobj.getString("phone"));
            txtAddress.setText(jobj.getString("address"));

            Picasso.with(this).load(jobj.getString("imgurl")).resize(250, 250)
                    .centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE).placeholder(R.drawable.ic_profile).into(imgProfile);
        }
    }
*/

}
