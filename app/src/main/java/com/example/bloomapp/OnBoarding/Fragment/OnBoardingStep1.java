package com.example.bloomapp.OnBoarding.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bloomapp.OnBoarding.OnBoardingScreen;
import com.example.bloomapp.R;
import android.widget.Button;

public class OnBoardingStep1 extends Fragment {

    public OnBoardingStep1() {
        // Required empty public constructor
    }

    public static OnBoardingStep1 newInstance() {
        return new OnBoardingStep1();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_on_boarding_step1, container, false);
        
        Button btnGetStarted = view.findViewById(R.id.btnGetStarted);
        if (btnGetStarted != null) {
            btnGetStarted.setOnClickListener(v -> {
                if (getActivity() instanceof OnBoardingScreen) {
                    ((OnBoardingScreen) getActivity()).nextStep();
                }
            });
        }
        
        return view;
    }
}