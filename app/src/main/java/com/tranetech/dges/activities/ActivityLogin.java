package com.tranetech.dges.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
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

public class ActivityLogin extends FragmentActivity {
    //   private static String mobile_s;
    private static final String PREFS_NAME = "Login";
    public static SharedPreferences settings;
    private static boolean hasLoggedIn;
    TextView txt1;
    ImageView image;
    EditText et_Mobile, et_password;
    // String GR_Number,str_gr_no;
    String mobile, password, strUrl, msg, mobile_s, password_s;
    View parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
     /*   getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        settings = getSharedPreferences(ActivityLogin.PREFS_NAME, 0);
        parentLayout = findViewById(android.R.id.content);
        image = (ImageView) findViewById(R.id.img_login);
        // et_GR_Number = (EditText) findViewById(R.id.et_gr_number);
        et_Mobile = (EditText) findViewById(R.id.et_number);
        et_password = (EditText) findViewById(R.id.et_password);

        hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

        // to hide soft keyboard until user interaction
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        if (hasLoggedIn) {
            Log.e("onCreate:has loged ", hasLoggedIn + "");
            Intent intent = new Intent(this, ActivityParentsMultiChild.class);
            intent.putExtra("mobile", mobile);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    public void btn_Login(View v) {
        mobile = et_Mobile.getText().toString();
        password = et_password.getText().toString();
        if (mobile.length() == 0) {
            et_Mobile.setError("Enter valid mobile number.");
        } else if (password.length() == 0) {
            et_password.setError("Enter valid password.");
        } else {
            GetData();
        }
    }

    public void GetData() {
        final ProgressDialog loading = ProgressDialog.show(this, "Login", "Please wait...", false, false);
        GetIP getIP = new GetIP();
        strUrl = getIP.updateip("login.php");
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
                        ErrorAlert.error(message, ActivityLogin.this);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //  params.put("grno", GR_Number);
                params.put("mobile", mobile);
                params.put("psw", password);
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

        try {
            jsonObject1 = new JSONObject(response);
            JSONArray jsonArray = jsonObject1.getJSONArray("list");
            Log.e("getjson:Response ", response);


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jobj = null;
                jobj = jsonArray.getJSONObject(i);

                msg = jobj.getString("msg");
                mobile_s = jobj.getString("mobile");
                password_s = jobj.getString("psw");
                Log.e("getjson: ", msg);
            }

            if (!msg.equals("1")) {
                Snackbar.make(getCurrentFocus(), "Your mobile number is not registered with DGES school.", Snackbar.LENGTH_LONG).show();
            } else {
                SharedPreferenceManager.setDefaults_boolean("hasLoggedIn", true, getApplicationContext());
                //str_gr_no = jobj.getString("grNo");

                Log.e("get mobile no: ", mobile_s);

                // SharedPreferenceManager.setDefaults("msg", msg, getApplicationContext());
                SharedPreferenceManager.setDefaults("mobile", mobile_s, getApplicationContext());

                SharedPreferences settings = getSharedPreferences(ActivityLogin.PREFS_NAME, 0); // 0 - for private mode
                SharedPreferences.Editor editor = settings.edit();
                //    editor.putString("msg", msg);
                editor.putString("mobile", mobile_s);
                editor.putString("password", password_s);
                editor.putBoolean("hasLoggedIn", true);
                editor.apply();


                Intent intent = new Intent(this, ActivityParentsMultiChild.class);
                intent.putExtra("mobile", mobile_s);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }


        } catch (JSONException e) {
            System.out.print(e.toString());
        }


    }


}
