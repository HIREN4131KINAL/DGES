package com.tranetech.dges.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.kosalgeek.android.caching.FileCacher;
import com.tranetech.dges.seter_geter.ParentChildData;
import com.tranetech.dges.R;

import java.io.IOException;
import java.util.List;

public class ActivityProfile extends AppCompatActivity {

    private TextView txtSname, txtClass, txtRollNo, txtGrNo, txtBday, txtGender, txtPhone, txtAddress;
    private ImageView imgProfile;
    private List<ParentChildData> parentChildDataList;
    private FileCacher<List<ParentChildData>> stringCacherList = new FileCacher<>(ActivityProfile.this, "cacheListTmp.txt");
    private ParentChildData parentChildData;
    private static final String PREFS_NAME = "ActivityProfile";
    public int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent mIntent = getIntent();
        position = mIntent.getIntExtra("position", 0);

        //getting all data of child in stringCacherList
        try {
            parentChildDataList = stringCacherList.readCache();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e("onCreate:Activity Profile ", String.valueOf(position));
        try {
            parentChildData = parentChildDataList.get(position);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LoaduiElements();
        SetAlldata();

    }

    private void SetAlldata() {
        txtSname.setText(parentChildData.getsName() + " " + parentChildData.getmName() + " " + parentChildData.getlName());
        txtClass.setText(parentChildData.getsStandard());
        txtRollNo.setText(parentChildData.getRollno());
        txtGrNo.setText(parentChildData.getGrNo());
        txtBday.setText(parentChildData.getDob());
        txtGender.setText(parentChildData.getGender());
        txtPhone.setText(parentChildData.getMobile());
        txtAddress.setText(parentChildData.getAddress());
        //  getPhoto();
    }


    private void getPhoto() {

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case android.R.id.home:
                Intent dashboard = new Intent(this, ActivityMainDashBord.class);
                startActivity(dashboard);
                break;
        }

        return true;
    }


}
