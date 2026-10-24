package com.example.bloomapp;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.example.bloomapp.ads.AdManager;
import com.google.android.gms.ads.MobileAds;

public class BloomApplication extends Application implements Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver {

    private Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Google Mobile Ads SDK
        MobileAds.initialize(this, initializationStatus -> {});

        // Register Activity Lifecycle Callbacks to track current Activity
        registerActivityLifecycleCallbacks(this);

        // Register Process Lifecycle Observer to handle Background -> Foreground transitions
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        // Preload App Open Ad
        AdManager.getInstance(this).initialize();
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onStart(owner);
        // App moved from background to foreground
        if (currentActivity != null) {
            AdManager.getInstance(this).showAppOpenAdIfEligible(currentActivity);
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        if (currentActivity == activity) {
            currentActivity = null;
        }
    }
}
