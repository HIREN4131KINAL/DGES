package com.tranetech.dges.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

/**
 * Created by HIREN AMALIYAR on 25-05-2016.
 */

public class MarshmallowPermissions {
    private Activity activity;

    public MarshmallowPermissions(Activity activity) {
        this.activity = activity;

    }

    public void requestpermissions() {
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }

    }


    public void AllowedManually(View vg) {
        Snackbar.make(vg, "You need storage permission", Snackbar.LENGTH_INDEFINITE)
                .setAction("Allow ME", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent i = new Intent();
                        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        i.addCategory(Intent.CATEGORY_DEFAULT);
                        i.setData(Uri.parse("package:" + activity.getPackageName()));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        activity.startActivity(i);
                    }

                })
                .show();

    }

    public boolean checkIfAlreadyhavePermission() {
        //  int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        //  int result1 = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        int result2 = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
//        int result3 = ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE);

        if ((result == PackageManager.PERMISSION_GRANTED || result1 == PackageManager.PERMISSION_GRANTED)) {
            return true;
        } else {
            return false;
        }

    }
}
