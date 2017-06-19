package com.tranetech.dges.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.tranetech.dges.R;

/**
 * Created by HIREN AMALIYAR on 25-05-2017.
 */

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends FragmentActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 6000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        final ImageView imageView = (ImageView) findViewById(R.id.image);
        final TextView textView = (TextView) findViewById(R.id.text);

        final Animation animation_1 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.bounce);
        imageView.startAnimation(animation_1);

        final Animation animation_2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_in);
        textView.startAnimation(animation_2);


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent intent = new Intent(SplashActivity.this, ActivityLogin.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }

}
