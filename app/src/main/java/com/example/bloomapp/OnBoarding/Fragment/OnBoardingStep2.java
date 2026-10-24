package com.example.bloomapp.OnBoarding.Fragment;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.bloomapp.OnBoarding.OnBoardingScreen;
import com.example.bloomapp.R;
import com.example.bloomapp.Screens.SplashScreenActivity;
import com.example.bloomapp.Utils.ButtonUtils;
import com.example.bloomapp.Utils.SharedPrefManager;
import com.google.android.material.button.MaterialButton;

public class OnBoardingStep2 extends Fragment {
    MaterialButton btnGetStarted;
    AppCompatEditText etFirstName;

    public OnBoardingStep2() {
        // Required empty public constructor
    }

    public static OnBoardingStep2 newInstance() {
        return new OnBoardingStep2();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_on_boarding_step2, container, false);
        SharedPrefManager prefManager = SharedPrefManager.getInstance(getContext());
        inItView(view);
        setupTextWatchers();
        ButtonUtils.updateButtonState(requireContext(), btnGetStarted, false);

        btnGetStarted.setOnClickListener(v -> {
            String firstName = etFirstName.getText().toString().trim();
            if (!firstName.isEmpty()) {
                prefManager.setString("user_first_name", firstName);
                if (getActivity() instanceof OnBoardingScreen) {
                    ((OnBoardingScreen) getActivity()).nextStep();
                }
            }
        });

        return view;
    }

    private void setupTextWatchers() {
        etFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean hasText = s != null && !s.toString().trim().isEmpty();
                ButtonUtils.updateButtonState(requireContext(), btnGetStarted, hasText);
            }
        });
    }

    private void inItView(View view) {
        btnGetStarted = view.findViewById(R.id.btnGetStarted);
        etFirstName = view.findViewById(R.id.etFirstName);
    }
}