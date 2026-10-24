package com.example.bloomapp.ads;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bloomapp.OnBoarding.OnBoardingScreen;
import com.example.bloomapp.R;
import com.example.bloomapp.Utils.SharedPrefManager;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

import java.util.Date;

public class AppOpenAdManager {

    private static final String TAG = "AppOpenAdManager";

    private AppOpenAd appOpenAd = null;
    private boolean isLoadingAd = false;
    private boolean isShowingAd = false;
    private long loadTime = 0;

    private final Context appContext;

    public AppOpenAdManager(Context context) {
        this.appContext = context.getApplicationContext();
    }

    public void loadAd() {
        if (isLoadingAd || isAdAvailable()) {
            return;
        }

        isLoadingAd = true;
        AdRequest request = new AdRequest.Builder().build();
        String adUnitId = appContext.getString(R.string.admob_app_open_id);

        AppOpenAd.load(
                appContext,
                adUnitId,
                request,
                new AppOpenAd.AppOpenAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull AppOpenAd ad) {
                        appOpenAd = ad;
                        isLoadingAd = false;
                        loadTime = new Date().getTime();
                        Log.d(TAG, "App Open Ad Loaded successfully.");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        appOpenAd = null;
                        isLoadingAd = false;
                        Log.e(TAG, "Failed to load App Open Ad: " + loadAdError.getMessage());
                    }
                }
        );
    }

    public boolean isAdAvailable() {
        boolean wasLoadedLessThan4HoursAgo = (new Date().getTime() - loadTime) < (4 * 3600 * 1000);
        return appOpenAd != null && wasLoadedLessThan4HoursAgo;
    }

    public boolean isShowingAd() {
        return isShowingAd;
    }

    public void showAdIfEligible(@NonNull Activity activity) {
        showAdIfEligible(activity, null);
    }

    public void showAdIfEligible(@NonNull Activity activity, Runnable onDismiss) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            Log.d(TAG, "Activity is invalid, skipping App Open Ad.");
            if (onDismiss != null) onDismiss.run();
            return;
        }

        if (isShowingAd) {
            Log.d(TAG, "An App Open Ad is already showing.");
            if (onDismiss != null) onDismiss.run();
            return;
        }

        // Edge Case: First-time user onboarding should NOT be interrupted
        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(activity);
        if (sharedPrefManager.isFirstTimeLaunch() || activity instanceof OnBoardingScreen) {
            Log.d(TAG, "First time launch / Onboarding active. Skipping App Open Ad.");
            if (onDismiss != null) onDismiss.run();
            return;
        }

        SharedPreferences prefs = appContext.getSharedPreferences(AdConfig.PREFS_NAME, Context.MODE_PRIVATE);
        long lastTime = prefs.getLong(AdConfig.KEY_LAST_APP_OPEN_TIME, 0);
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastTime < AdConfig.APP_OPEN_COOLDOWN) {
            Log.d(TAG, "App Open Ad is on cooldown.");
            if (onDismiss != null) onDismiss.run();
            return;
        }

        if (!isAdAvailable()) {
            Log.d(TAG, "App Open Ad is not available yet. Preloading...");
            loadAd();
            if (onDismiss != null) onDismiss.run();
            return;
        }

        appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                appOpenAd = null;
                isShowingAd = false;
                Log.d(TAG, "App Open Ad dismissed.");
                prefs.edit().putLong(AdConfig.KEY_LAST_APP_OPEN_TIME, System.currentTimeMillis()).apply();
                loadAd();
                if (onDismiss != null) onDismiss.run();
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                appOpenAd = null;
                isShowingAd = false;
                Log.e(TAG, "App Open Ad failed to show: " + adError.getMessage());
                loadAd();
                if (onDismiss != null) onDismiss.run();
            }

            @Override
            public void onAdShowedFullScreenContent() {
                isShowingAd = true;
                Log.d(TAG, "App Open Ad showed full screen content.");
            }
        });

        isShowingAd = true;
        appOpenAd.show(activity);
    }
}
