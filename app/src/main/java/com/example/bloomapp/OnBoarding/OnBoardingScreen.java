package com.example.bloomapp.OnBoarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.bloomapp.MainActivity;
import com.example.bloomapp.OnBoarding.Fragment.OnBoardingStep1;
import com.example.bloomapp.OnBoarding.Fragment.OnBoardingStep2;
import com.example.bloomapp.OnBoarding.Fragment.OnBoardingStep3;
import com.example.bloomapp.OnBoarding.Fragment.OnBoardingStep4;
import com.example.bloomapp.R;
import com.example.bloomapp.Utils.SharedPrefManager;

public class OnBoardingScreen extends AppCompatActivity {

    public static final int TOTAL_STEPS = 4;
    private static final String KEY_CURRENT_STEP = "key_current_step";

    private ImageView btnBack;
    private View step1Indicator, step2Indicator, step3Indicator, step4Indicator;
    private int currentStep = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_on_boarding_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupListeners();

        if (savedInstanceState != null) {
            currentStep = savedInstanceState.getInt(KEY_CURRENT_STEP, 1);
            updateUiForStep(currentStep);
        } else {
            navigateToStep(1);
        }
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        step1Indicator = findViewById(R.id.step1Indicator);
        step2Indicator = findViewById(R.id.step2Indicator);
        step3Indicator = findViewById(R.id.step3Indicator);
        step4Indicator = findViewById(R.id.step4Indicator);
    }

    private void setupListeners() {
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> previousStep());
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (currentStep > 1) {
                    previousStep();
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    public void navigateToStep(int step) {
        if (step < 1 || step > TOTAL_STEPS) {
            return;
        }

        Fragment fragment;
        switch (step) {
            case 1:
                fragment = new OnBoardingStep1();
                break;
            case 2:
                fragment = new OnBoardingStep2();
                break;
            case 3:
                fragment = new OnBoardingStep3();
                break;
            case 4:
                fragment = new OnBoardingStep4();
                break;
            default:
                return;
        }

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                )
                .replace(R.id.onboardingContainer, fragment)
                .commit();

        currentStep = step;
        updateUiForStep(currentStep);
    }

    public void nextStep() {
        if (currentStep < TOTAL_STEPS) {
            navigateToStep(currentStep + 1);
        } else {
            finishOnboarding();
        }
    }

    public void previousStep() {
        if (currentStep > 1) {
            navigateToStep(currentStep - 1);
        }
    }

    public void finishOnboarding() {
        SharedPrefManager.getInstance(this).setFirstTimeLaunch(false);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void updateUiForStep(int step) {
        if (btnBack != null) {
            btnBack.setVisibility(step > 1 ? View.VISIBLE : View.GONE);
        }

        if (step1Indicator != null) {
            step1Indicator.setBackgroundResource(step >= 1 ? R.drawable.bg_progress_active : R.drawable.bg_progress_inactive);
        }
        if (step2Indicator != null) {
            step2Indicator.setBackgroundResource(step >= 2 ? R.drawable.bg_progress_active : R.drawable.bg_progress_inactive);
        }
        if (step3Indicator != null) {
            step3Indicator.setBackgroundResource(step >= 3 ? R.drawable.bg_progress_active : R.drawable.bg_progress_inactive);
        }
        if (step4Indicator != null) {
            step4Indicator.setBackgroundResource(step >= 4 ? R.drawable.bg_progress_active : R.drawable.bg_progress_inactive);
        }
    }

    public int getCurrentStep() {
        return currentStep;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_STEP, currentStep);
    }
}