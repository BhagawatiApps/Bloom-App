package com.example.bloomapp;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.bloomapp.Utils.SharedPrefManager;
import com.example.bloomapp.ads.AdManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private SharedPrefManager prefManager;
    private TextView tvGreeting, tvUserName;
    private FrameLayout bannerAdContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = SharedPrefManager.getInstance(getApplicationContext());
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inItView();
        setUpGreetingData();
        loadBannerAd();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        }
    }

    private void setUpGreetingData() {
        String greeting = getGreeting();
        tvGreeting.setText(String.format(Locale.getDefault(), "%s,", greeting));
        tvGreeting.setAllCaps(true);

        String userName = prefManager.getString("user_first_name");
        tvUserName.setText(userName);
    }

    private String getGreeting() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        String greetingMessage;

        if (hour >= 4 && hour < 12) {
            greetingMessage = "Good Morning ";
        } else if (hour >= 12 && hour < 17) {
            greetingMessage = "Good Afternoon ";
        } else if (hour >= 17 && hour < 22) {
            greetingMessage = "Good Evening ";
        } else {
            greetingMessage = "Good Night ";
        }
        return greetingMessage;
    }

    private void loadBannerAd() {
        if (bannerAdContainer != null) {
            AdManager.getInstance(this).loadBanner(this, bannerAdContainer);
        }
    }

    private void inItView() {
        tvGreeting = findViewById(R.id.tvGreeting);
        tvUserName = findViewById(R.id.tvUserName);
        bannerAdContainer = findViewById(R.id.banner_ad_container);
    }
}
