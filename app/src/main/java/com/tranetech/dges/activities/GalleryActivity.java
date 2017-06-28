package com.tranetech.dges.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
import com.tranetech.dges.adapters.GalleryAdapter;
import com.tranetech.dges.seter_geter.GalleryData;
import com.tranetech.dges.utils.ErrorAlert;
import com.tranetech.dges.utils.GetIP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private List<GalleryData> galleryDatas = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private GalleryAdapter galleryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Gallery");

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sr_gallery);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_gallery);
    }

    @Override
    public void onRefresh() {
        if (galleryAdapter != null) {
            galleryAdapter.clear();
            getData();
            galleryAdapter.addALL(galleryDatas);
            swipeRefreshLayout.setRefreshing(false);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            ErrorAlert.error("No data available", GalleryActivity.this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (galleryAdapter != null) {
            galleryAdapter.clear();
        }
        getData();
    }

    private void getData() {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading data...", "Please wait...", false, false);

        RequestQueue queue = Volley.newRequestQueue(this);
        GetIP getIP = new GetIP();
        String url = getIP.updateip("gallery.php");

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        if (galleryAdapter != null) {
                            galleryAdapter.clear();
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
                ErrorAlert.error(message, GalleryActivity.this);
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getJson(String response) throws JSONException, IOException {

        JSONObject jsonObject = new JSONObject(response);

        JSONArray jsonArray = jsonObject.getJSONArray("list");

        for (int i = 0; i < jsonArray.length(); i++) {

            GalleryData galleryData = new GalleryData();

            JSONObject jobj = jsonArray.getJSONObject(i);
            galleryData.setStrAlbumDate(jobj.getString("date"));
            galleryData.setStrAlbumTitle(jobj.getString("name"));
            galleryData.setStrAlbumCount(jobj.getString("count"));
            galleryData.setStrAlbumImgUrl(jobj.getString("imgPath"));
            galleryData.setsGTid(jobj.getString("gtId"));

            galleryDatas.add(galleryData);
        }
        IntialAdapter();
    }

    public void IntialAdapter() {
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(GalleryActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        galleryAdapter = new GalleryAdapter(galleryDatas, this);
       /* recyclerView.scrollToPosition(circularDatas.size() + 1);
        circularAdapter.notifyItemInserted(circularDatas.size() + 1);*/
        recyclerView.setAdapter(galleryAdapter);
    }
}
