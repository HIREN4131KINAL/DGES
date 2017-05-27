package com.tranetech.dges;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ActivityMainDashBord extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dash_bord);

        // get action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Dash Board");
    }

    public void cardProfile(View v) {

        Intent Profile = new Intent(this, ProfileActivity.class);
        startActivity(Profile);

    }


    public void cardResult(View v) {

        Intent Result = new Intent(this, ProfileActivity.class);
        startActivity(Result);

    }

    public void cardFees(View v) {

        Intent Fees = new Intent(this, ProfileActivity.class);
        startActivity(Fees);

    }

    public void LinearProfile(View v) {
        Intent Profile = new Intent(this, ProfileActivity.class);
        startActivity(Profile);

    }

    public void cardHomework(View v) {
        Intent Homework = new Intent(this, HomeworkActivity.class);
        startActivity(Homework);
    }

    public void cardCircular(View v) {

        Intent Circular = new Intent(this, CircularActivity.class);
        startActivity(Circular);

    }

    public void cardUpdates(View v) {

        Intent Updates = new Intent(this, CircularActivity.class);
        startActivity(Updates);

    }

    public void cardObservation(View v) {

        Intent Updates = new Intent(this, ObservationActivity.class);
        startActivity(Updates);

    }

    public void cardPolicies(View v) {

        Intent Policies = new Intent(this, ProfileActivity.class);
        startActivity(Policies);
    }


}
