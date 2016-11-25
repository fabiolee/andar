package edu.dhbw.andobjviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import edu.dhbw.andarmodelviewer.R;
import edu.dhbw.andobjviewer.homepage.HomeActivity;

/**
 * Created by Edwin on 24/11/2016.
 */

public class SplashActivity extends Activity {
    private final int SPLASH_TIMER = 3000;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override public void run() {
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            }
        }, SPLASH_TIMER);
    }
}
