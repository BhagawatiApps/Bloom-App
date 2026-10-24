package com.example.bloomapp.ads;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.example.bloomapp.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

public class BannerAdHelper {

    private static final String TAG = "BannerAdHelper";

    public static AdView loadAdaptiveBanner(Activity activity, FrameLayout adContainerView) {
        if (activity == null || adContainerView == null) {
            return null;
        }

        try {
            AdView adView = new AdView(activity);
            adView.setAdUnitId(activity.getString(R.string.admob_banner_id));

            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    if (adContainerView != null) {
                        adContainerView.setVisibility(View.VISIBLE);
                    }
                    Log.d(TAG, "Adaptive banner loaded successfully.");
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    if (adContainerView != null) {
                        adContainerView.setVisibility(View.GONE);
                    }
                    Log.e(TAG, "Failed to load adaptive banner: " + loadAdError.getMessage());
                }
            });

            DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
            float density = displayMetrics.density;
            int adWidthPixels = adContainerView.getWidth();
            if (adWidthPixels == 0) {
                adWidthPixels = displayMetrics.widthPixels;
            }
            int adWidth = Math.round(adWidthPixels / density);

            AdSize adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
            adView.setAdSize(adSize);

            adContainerView.removeAllViews();
            adContainerView.addView(adView);

            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

            return adView;
        } catch (Exception e) {
            Log.e(TAG, "Error loading banner ad: " + e.getMessage());
            if (adContainerView != null) {
                adContainerView.setVisibility(View.GONE);
            }
            return null;
        }
    }
}
