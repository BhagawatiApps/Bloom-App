package com.example.bloomapp.ads;

import android.app.Activity;
import android.content.Context;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdView;

public class AdManager {

    private static AdManager instance;

    private final AppOpenAdManager appOpenAdManager;

    public static synchronized AdManager getInstance(Context context) {
        if (instance == null) {
            instance = new AdManager(context.getApplicationContext());
        }
        return instance;
    }

    private AdManager(Context context) {
        appOpenAdManager = new AppOpenAdManager(context);
    }

    public void initialize() {
        appOpenAdManager.loadAd();
    }

    public AppOpenAdManager getAppOpenAdManager() {
        return appOpenAdManager;
    }

    public void showAppOpenAdIfEligible(Activity activity) {
        appOpenAdManager.showAdIfEligible(activity);
    }

    public void showAppOpenAdIfEligible(Activity activity, Runnable onDismiss) {
        appOpenAdManager.showAdIfEligible(activity, onDismiss);
    }

    public AdView loadBanner(Activity activity, FrameLayout bannerContainer) {
        return BannerAdHelper.loadAdaptiveBanner(activity, bannerContainer);
    }
}
