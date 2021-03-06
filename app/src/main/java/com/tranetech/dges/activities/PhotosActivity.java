package com.tranetech.dges.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

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
import com.tranetech.dges.adapters.PhotosAdapter;
import com.tranetech.dges.seter_geter.PhotoData;
import com.tranetech.dges.utils.ErrorAlert;
import com.tranetech.dges.utils.GetIP;
import com.tranetech.dges.adapters.SlideshowDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PhotosActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    PhotoData photoData;
    ArrayList<PhotoData> photoDatas;
    private RecyclerView mRVPhotos;
    private PhotosAdapter mAdapter;
    private Intent mIntent;
    private String sGTId;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        photoDatas = new ArrayList<>();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sr_photos);
        swipeRefreshLayout.setOnRefreshListener(this);
        mRVPhotos = (RecyclerView) findViewById(R.id.rv_photos);
        mIntent = getIntent();
        sGTId = mIntent.getStringExtra("gtid");

        mRVPhotos.addOnItemTouchListener(new PhotosAdapter.RecyclerTouchListener(getApplicationContext(), mRVPhotos, new PhotosAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", photoDatas);
                bundle.putInt("position", position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    @Override
    public void onRefresh() {
        if (mAdapter != null) {
            mAdapter.clear();
            getData(sGTId);
            mAdapter.addALL(photoDatas);
            swipeRefreshLayout.setRefreshing(false);
        } else {
            getData(sGTId);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.clear();
        }
        getData(sGTId);
    }

    private void getData(final String sGTId) {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading data...", "Please wait...", false, false);
        GetIP getIP = new GetIP();
        String strUrl = getIP.updateip("gallery_view.php");
        StringRequest postRequest = new StringRequest(Request.Method.POST, strUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response StudentAll data : ", response);
                        if (mAdapter != null) {
                            mAdapter.clear();
                        }
                        loading.dismiss();
                        try {
                            getJson(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();

                        IntialAdapter();

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
                        ErrorAlert.error(message, PhotosActivity.this);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("gtid", sGTId);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);
    }

    private void getJson(String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
               PhotoData photoData = new PhotoData();
                JSONObject jobj = jsonArray.getJSONObject(i);
                photoData.setsGid(jobj.getString("gId"));
                photoData.setsImgDate(jobj.getString("date"));
                photoData.setsPhotoTitle(jobj.getString("title"));
                photoData.setsPhotoImgURL(jobj.getString("image"));
                photoData.setsPhotoImgURLLarge(jobj.getString("limage"));

                photoDatas.add(photoData);
            }

        IntialAdapter();
    }

    public void IntialAdapter() {


        mRVPhotos.setHasFixedSize(true);
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        int numberOfColumns = 2;
        mRVPhotos.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
//        mRVGallery.setLayoutManager(mLayoutManager);
        mAdapter = new PhotosAdapter(photoDatas, PhotosActivity.this);
       /* recyclerView.scrollToPosition(circularDatas.size() + 1);
        circularAdapter.notifyItemInserted(circularDatas.size() + 1);*/
        mRVPhotos.setAdapter(mAdapter);
    }
}
