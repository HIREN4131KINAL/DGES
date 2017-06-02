package com.tranetech.dges.activities;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.kosalgeek.android.caching.FileCacher;
import com.tranetech.dges.adapters.AdapterParentsMultiChild;
import com.tranetech.dges.seter_geter.ParentChildData;
import com.tranetech.dges.R;
import com.tranetech.dges.utils.MarshmallowPermissions;
import com.tranetech.dges.utils.SharedPreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HIREN AMALIYAR on 27-05-2017.
 */

public class ActivityParentsMultiChild extends AppCompatActivity {
    private SharedPreferenceManager preferenceManager;
    private List<ParentChildData> parentChildDataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterParentsMultiChild adapterParentsMultiChild;
    private FileCacher<String> stringCacher = new FileCacher<>(ActivityParentsMultiChild.this, "cache_tmp.txt");
    private FileCacher<List<ParentChildData>> stringCacherList = new FileCacher<>(ActivityParentsMultiChild.this, "cacheListTmp.txt");
    private FileCacher<ParentChildData> storeObj = new FileCacher<>(ActivityParentsMultiChild.this, "SorageOFobj.txt");
    private String response;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_multi_child);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Child");
        recyclerView = (RecyclerView) findViewById(R.id.rv_parents_multi_stu);
        preferenceManager = new SharedPreferenceManager();

        try {
            response = stringCacher.readCache();
            getJson(response);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (storeObj.hasCache()) {
            try {
                storeObj.clearCache();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    public void getJson(String response) throws JSONException, IOException {
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
                parentChildData.setsStandard_ID(jobj.getString("stdid"));
                parentChildData.setDivision(jobj.getString("div"));
                parentChildData.setAdhar(jobj.getString("adhar"));
                parentChildData.setGrNo(jobj.getString("grNo"));
                parentChildData.setRollno(jobj.getString("rollNo"));
                parentChildData.setAddress(jobj.getString("address"));
                parentChildData.setMobile(jobj.getString("mobile"));
                parentChildData.setDob(jobj.getString("dob"));

                parentChildData.setGender(jobj.getString("gender"));
                parentChildData.seBloodgroop(jobj.getString("bloodgroup"));
                parentChildData.setNationality(jobj.getString("nationality"));
                parentChildData.setPhHndicap(jobj.getString("handi"));
                parentChildData.setCategory(jobj.getString("category"));
                parentChildData.setPhoto(jobj.getString("photo"));
                parentChildData.setLastschool(jobj.getString("lastschool"));
                parentChildData.setLaststd(jobj.getString("laststd"));
                parentChildData.setPercentage(jobj.getString("percentage"));
                parentChildData.setStatus(jobj.getString("status"));
                parentChildData.setMessage(jobj.getString("msg"));


                parentChildDataList.add(parentChildData);
                stringCacherList.writeCache(parentChildDataList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        IntialAdapter();
    }

    public void IntialAdapter() {
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ActivityParentsMultiChild.this);
        recyclerView.setLayoutManager(mLayoutManager);
        adapterParentsMultiChild = new AdapterParentsMultiChild(parentChildDataList, getApplicationContext());
       /* recyclerView.scrollToPosition(parentChildDataList.size() + 1);
        adapterParentsMultiChild.notifyItemInserted(parentChildDataList.size() + 1);*/
        recyclerView.setAdapter(adapterParentsMultiChild);
    }

    /**
     * Created by HIREN AMALIYAR on 25-05-2017.
     */

    public static class ActivityPermission extends Activity {
        MarshmallowPermissions marsh;
        Intent getiIntent;
        View parentLayout;
        private static final int REQUEST_PERMISSION = 1;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            setContentView(R.layout.activity_permission);
            marsh = new MarshmallowPermissions(this);
            parentLayout = findViewById(android.R.id.content);
            marsh = new MarshmallowPermissions(ActivityPermission.this);
            parentLayout = findViewById(android.R.id.content);
            getiIntent = getIntent();


            if (!marsh.checkIfAlreadyhavePermission()) {
                marsh.requestpermissions();
            } else {
                Intent intent = new Intent(this, ActivityMainDashBord.class);
                startActivity(intent);
                finish();
            }


        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        public void onRequestPermissionsResult(final int requestCode, String[] permissions, int[] grantResults) {
            if (requestCode == REQUEST_PERMISSION) {
                // for each permission check if the user grantet/denied them
                // you may want to group the rationale in a single dialog,
                // this is just an tranetech
                for (int i = 0, len = permissions.length; i < len; i++) {
                    final String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean showRationale = shouldShowRequestPermissionRationale(permission);
                        if (!showRationale) {
                            // user denied flagging NEVER ASK AGAIN
                            // you can either enable some fall back,
                            // disable features of your app
                            // or open another dialog explaining
                            // again the permission and directing to
                            // the app setting
                            marsh.AllowedManually(parentLayout);
                        } else if (android.Manifest.permission.ACCESS_FINE_LOCATION.equals(permission)) {
                            // showRationale(permission, R.string.permission_denied);
                            // user denied WITHOUT never ask again
                            // this is a good place to explain the user
                            // why you need the permission and ask if he want
                            // to accept it (the rationale)
                            marsh.AllowedManually(parentLayout);
                        }

                    } else {
                        if (marsh.checkIfAlreadyhavePermission()) {
                            Intent intent = new Intent(this, ActivityMainDashBord.class);
                            startActivity(intent);
                            finish();
                        } else {
                            marsh.AllowedManually(parentLayout);
                        }
                    }
                }
            }

        }

        @Override
        protected void onResume() {
            super.onResume();
            if (marsh.checkIfAlreadyhavePermission()) {
                Intent intent = new Intent(this, ActivityPermission.class);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected void onRestart() {
            super.onRestart();
            if (!marsh.checkIfAlreadyhavePermission()) {
                marsh.requestpermissions();
            }
        }


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
        builder.setIcon(R.drawable.logo);
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
