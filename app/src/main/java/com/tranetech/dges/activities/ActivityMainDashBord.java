package com.tranetech.dges.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.android.caching.FileCacher;
import com.tranetech.dges.R;
import com.tranetech.dges.seter_geter.ParentChildData;
import com.tranetech.dges.utils.SharedPreferenceManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivityMainDashBord extends AppCompatActivity {
    private SharedPreferenceManager preferenceManager;
    public static int intValue;
    private List<ParentChildData> parentChildDataList;
    private FileCacher<List<ParentChildData>> stringCacherList = new FileCacher<>(ActivityMainDashBord.this, "cacheListTmp.txt");
    private ParentChildData parentChildData;
    private FileCacher<Integer> storePosition = new FileCacher<>(ActivityMainDashBord.this, "SorageOFposition");
    Intent mIntent;
    Integer getposition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dash_bord);
        preferenceManager = new SharedPreferenceManager();


        // get action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Dash Board");


        //getting all data of child in stringCacherList
        try {
            parentChildDataList = stringCacherList.readCache();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            getposition = storePosition.readCache();
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (storePosition.hasCache()) {
            intValue = getposition;
        } else {
            mIntent = getIntent();
            intValue = mIntent.getIntExtra("position", 0);
            try {
                storePosition.writeCache(intValue);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        parentChildData = parentChildDataList.get(intValue);
        LoadUIelements();
    }


    private void LoadUIelements() {
        TextView txt_sname = (TextView) findViewById(R.id.txt_sname);
        txt_sname.setText(parentChildData.getsName() + " " + parentChildData.getmName() + " " + parentChildData.getlName());
        //name = parentChildData.getsName() + " " + parentChildData.getmName() + " " + parentChildData.getlName();
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
        Profile.putExtra("position", intValue);
        Profile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Profile);

    }

    public void cardHomework(View v) {
        //have to send standard id
        Intent Homework = new Intent(this, ActivityHomework.class);
        Homework.putExtra("standard", parentChildData.getsStandard_ID());
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
        builder.setIcon(R.drawable.logo);
        builder.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        preferenceManager = new SharedPreferenceManager();
                        preferenceManager.ClearAllPreferences(getApplicationContext());

                        ActivityLogin.settings.edit().clear().apply();

                        Intent toLoginActivity = new Intent(ActivityMainDashBord.this, ActivityLogin.class);
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
