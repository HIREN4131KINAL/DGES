package com.tranetech.dges.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tranetech.dges.R;
import com.tranetech.dges.utils.SharedPreferenceManager;

public class ActivityMainDashBord extends AppCompatActivity {
    private SharedPreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dash_bord);

        // get action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Dash Board");
    }


    public void cardResult(View v) {

        Intent Result = new Intent(this, ActivityResult.class);
        startActivity(Result);

    }

    public void cardFees(View v) {

        Intent Fees = new Intent(this, ActivityFees.class);
        startActivity(Fees);

    }

    public void LinearProfile(View v) {
        Intent Profile = new Intent(this, ActivityProfile.class);
        startActivity(Profile);

    }

    public void cardHomework(View v) {
        Intent Homework = new Intent(this, ActivityHomework.class);
        startActivity(Homework);
    }

    public void cardCircular(View v) {

        Intent Circular = new Intent(this, ActivityCircular.class);
        startActivity(Circular);

    }


    public void cardObservation(View v) {

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
