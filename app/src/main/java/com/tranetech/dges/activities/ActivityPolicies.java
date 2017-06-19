package com.tranetech.dges.activities;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.webkit.WebView;
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
import com.kosalgeek.android.caching.FileCacher;
import com.tranetech.dges.R;
import com.tranetech.dges.seter_geter.PoliciesData;
import com.tranetech.dges.utils.ErrorAlert;
import com.tranetech.dges.utils.GetIP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivityPolicies extends AppCompatActivity {

    private List<PoliciesData> policiesDataList = new ArrayList<>();
    private FileCacher<List<PoliciesData>> CatchPolicies = new FileCacher<>(ActivityPolicies.this, "PoliciesData.txt");
    TextView txt_policies_tital;
    private PoliciesData policiesDataData;
    WebView web_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_policies);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Policies");

        txt_policies_tital = (TextView) findViewById(R.id.txt_policies_tital);
        web_description = (WebView) findViewById(R.id.web_description);
    }

    @Override
    protected void onResume() {
        getData();
        super.onResume();
    }

    private void getData() {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading data...", "Please wait...", false, false);

        RequestQueue queue = Volley.newRequestQueue(this);
        GetIP getIP = new GetIP();
        String url = getIP.updateip("policy.php");

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        Log.e("onResponse: ", response);
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
                    if (CatchPolicies.hasCache()) {
                        policiesDataList = CatchPolicies.readCache();
                        setData();
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
                ErrorAlert.error(message, ActivityPolicies.this);
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getJson(String response) throws JSONException, IOException {

        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("list");

        for (int i = 0; i < jsonArray.length(); i++) {

            policiesDataData = new PoliciesData();

            JSONObject jobj = jsonArray.getJSONObject(i);
            policiesDataData.setsPolicesTitle(jobj.getString("title"));
            policiesDataData.setsPolicesDesc(jobj.getString("description"));

            policiesDataList.add(policiesDataData);
            CatchPolicies.writeCache(policiesDataList);

        }
        setData();
    }

    private void setData() {

        String str = policiesDataList.get(0).getsPolicesTitle();
        Log.e("setData: ", str);

        String str2 = policiesDataList.get(0).getsPolicesDesc();
        Log.e("setDesc: ", str2);

        txt_policies_tital.setText(str);
        web_description.loadDataWithBaseURL(null, str2, "text/html", "utf-8", null);

    }

}
