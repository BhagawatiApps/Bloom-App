package com.example.bloomapp.Screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bloomapp.MainActivity;
import com.example.bloomapp.OnBoarding.OnBoardingScreen;
import com.example.bloomapp.R;
import com.example.bloomapp.Utils.SharedPrefManager;
import com.example.bloomapp.ads.AdManager;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    private boolean isNavigated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (isFinishing() || isDestroyed() || isNavigated) {
                return;
            }

            SharedPrefManager prefManager = SharedPrefManager.getInstance(SplashScreenActivity.this);
            if (prefManager.isFirstTimeLaunch()) {
                navigateToOnboarding();
            } else {
                AdManager.getInstance(getApplicationContext()).showAppOpenAdIfEligible(
                        SplashScreenActivity.this,
                        this::navigateToMain
                );
            }
        }, 3000);
    }

    private void navigateToOnboarding() {
        if (isNavigated) return;
        isNavigated = true;
        Intent intent = new Intent(SplashScreenActivity.this, OnBoardingScreen.class);
        startActivity(intent);
        finish();
    }

    private void navigateToMain() {
        if (isNavigated) return;
        isNavigated = true;
        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
