package com.tranetech.dges.activities;

/**
 * Created by HIREN AMALIYAR on 03-06-2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tranetech.dges.R;
import com.tranetech.dges.utils.MarshmallowPermissions;


public class ActivityPermission extends Activity {
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
            Intent intent = new Intent(this, ActivityParentsMultiChild.class);
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
                        Intent intent = new Intent(this, ActivityParentsMultiChild.class);
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