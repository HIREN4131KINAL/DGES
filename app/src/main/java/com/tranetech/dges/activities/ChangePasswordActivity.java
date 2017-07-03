package com.tranetech.dges.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.tranetech.dges.R;
import com.tranetech.dges.utils.ErrorAlert;
import com.tranetech.dges.utils.GetIP;
import com.tranetech.dges.utils.SharedPreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText etOldPass, etNewPass;
    private String sOldPass, sNewPass, sMobile;
    private SharedPreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        preferenceManager = new SharedPreferenceManager();
        etOldPass = (EditText) findViewById(R.id.et_old_password);
        etNewPass = (EditText) findViewById(R.id.et_new_password);
    }

    public void btn_submit(View v) {
        sMobile = preferenceManager.getDefaults("mobile", getApplicationContext());
        sOldPass = etOldPass.getText().toString();
        sNewPass = etNewPass.getText().toString();
        if (sOldPass.length() == 0) {
            etOldPass.setError("Enter valid mobile number.");
        } else if (sNewPass.length() == 0) {
            etNewPass.setError("Enter valid password.");
        } else {
            GetData();
        }
    }

    public void GetData() {
        final ProgressDialog loading = ProgressDialog.show(this, "Login", "Please wait...", false, false);
        GetIP getIP = new GetIP();
        String strUrl = getIP.updateip("updatepsw.php");
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
                        Log.e("Response Loin data : ", response);
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
                        ErrorAlert.error(message, ChangePasswordActivity.this);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //  params.put("grno", GR_Number);
                params.put("mobile", sMobile);
                params.put("oldpsw", sOldPass);
                params.put("newpsw", sNewPass);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);
    }

    protected void getjson(String response) throws IOException {
        JSONObject jsonObject1 = null;
        // store response in cache memory
        //   stringCacher.writeCache(response);
        String msg = null, mobile_s, sNewPassword;
        try {
            jsonObject1 = new JSONObject(response);
            JSONArray jsonArray = jsonObject1.getJSONArray("list");
            Log.e("getjson:Response ", response);


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jobj = null;
                jobj = jsonArray.getJSONObject(i);

                msg = jobj.getString("msg");
                mobile_s = jobj.getString("mobile");
                sNewPassword = jobj.getString("newpsw");
                Log.e("getjson: ", msg);
            }

            if (!msg.equals("1")) {
                ErrorAlert.error("Something went wrong. please try again later", ChangePasswordActivity.this);

            } else {
                preferenceManager = new SharedPreferenceManager();
                preferenceManager.ClearAllPreferences(getApplicationContext());

                ActivityLogin.settings.edit().clear().apply();

                Toast.makeText(getApplicationContext(), "Successfully Changed", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, ActivityLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

        } catch (JSONException e) {
            System.out.print(e.toString());
        }


    }
}
