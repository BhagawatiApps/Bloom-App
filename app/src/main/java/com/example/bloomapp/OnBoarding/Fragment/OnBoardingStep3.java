package com.example.bloomapp.OnBoarding.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloomapp.OnBoarding.OnBoardingAdapters.HabitLisAdapter;
import com.example.bloomapp.OnBoarding.OnBoardingModels.HabitListModel;
import com.example.bloomapp.OnBoarding.OnBoardingScreen;
import com.example.bloomapp.R;
import com.example.bloomapp.Utils.ButtonUtils;
import com.example.bloomapp.Utils.HabitManager;
import com.example.bloomapp.Utils.SharedPrefManager;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class OnBoardingStep3 extends Fragment {
    private MaterialButton btnGetStarted;
    private RecyclerView rvGoalsList;
    private HabitLisAdapter habitLisAdapter;
    private ArrayList<HabitListModel> habitList;

    public OnBoardingStep3() {
        // Required empty public constructor
    }

    public static OnBoardingStep3 newInstance() {
        return new OnBoardingStep3();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_on_boarding_step3, container, false);
        SharedPrefManager prefManager = SharedPrefManager.getInstance(getContext());
        inItView(view);
        setupRecyclerView();

        ButtonUtils.updateButtonState(requireContext(), btnGetStarted, true);

        btnGetStarted.setOnClickListener(v -> {
            if (getActivity() instanceof OnBoardingScreen) {
                ((OnBoardingScreen) getActivity()).nextStep();
            }
        });

        return view;
    }

    private void inItView(View view) {
        btnGetStarted = view.findViewById(R.id.btnGetStarted);
        rvGoalsList = view.findViewById(R.id.rvGoalsList);
    }

    private void setupRecyclerView() {
        if (rvGoalsList == null) return;

        rvGoalsList.setLayoutManager(new LinearLayoutManager(requireContext()));

        habitList = HabitManager.getDefaultHabits(requireContext());

        habitLisAdapter = new HabitLisAdapter(requireContext(), habitList);
        rvGoalsList.setAdapter(habitLisAdapter);
    }
}