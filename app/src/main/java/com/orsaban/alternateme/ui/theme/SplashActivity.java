package com.orsaban.alternateme.ui.theme;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.orsaban.alternateme.R;

public class SplashActivity extends AppCompatActivity {

    // Duration for the splash screen (in milliseconds)
    private static final int SPLASH_DELAY = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("SplashActivity", "onCreate started");
        super.onCreate(savedInstanceState);
        // Set the splash screen layout
        setContentView(R.layout.activity_splash);
        Log.d("SplashActivity", "SplashActivity started");

        // Find views in the layout
        ImageView logo = findViewById(R.id.logo);
        TextView tagline = findViewById(R.id.tagline);

        // Load the fade-in animation from resources
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        // Start the animation on both the logo and tagline
        logo.startAnimation(fadeIn);
        tagline.startAnimation(fadeIn);

        // Use a Handler to delay the transition to the next activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the next activity (e.g., CreateScenarioActivity)
                Intent intent = new Intent(SplashActivity.this, CreateScenarioActivity.class);
                startActivity(intent);
                finish(); // Close the splash screen so the user cannot return to it.
            }
        }, SPLASH_DELAY);
    }
}
