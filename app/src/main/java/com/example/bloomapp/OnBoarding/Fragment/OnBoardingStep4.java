package com.example.bloomapp.OnBoarding.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.bloomapp.OnBoarding.OnBoardingScreen;
import com.example.bloomapp.R;
import com.example.bloomapp.Utils.SharedPrefManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Locale;

public class OnBoardingStep4 extends Fragment {

    private ConstraintLayout cardMorning, cardAfternoon, cardEvening;
    private RadioButton rbMorning, rbAfternoon, rbEvening;
    private AppCompatEditText etCustomTime;
    private MaterialButton btnGetStarted;
    private android.widget.TextView tvSkipSetup;
    private SharedPrefManager prefManager;

    private String selectedReminder = "7:30 AM"; // Default to morning

    public OnBoardingStep4() {
        // Required empty public constructor
    }

    public static OnBoardingStep4 newInstance() {
        return new OnBoardingStep4();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_on_boarding_step4, container, false);
        prefManager = SharedPrefManager.getInstance(requireContext());

        initViews(view);
        setupListeners();

        // Default select morning
        selectPreset("morning");

        return view;
    }

    private void initViews(View view) {
        cardMorning = view.findViewById(R.id.cardMorning);
        cardAfternoon = view.findViewById(R.id.cardAfternoon);
        cardEvening = view.findViewById(R.id.cardEvening);

        rbMorning = view.findViewById(R.id.rbMorning);
        rbAfternoon = view.findViewById(R.id.rbAfternoon);
        rbEvening = view.findViewById(R.id.rbEvening);

        etCustomTime = view.findViewById(R.id.etCustomTime);
        btnGetStarted = view.findViewById(R.id.btnGetStarted);
        tvSkipSetup = view.findViewById(R.id.tvSkipSetup);
    }

    private void setupListeners() {
        if (cardMorning != null) cardMorning.setOnClickListener(v -> selectPreset("morning"));
        if (cardAfternoon != null) cardAfternoon.setOnClickListener(v -> selectPreset("afternoon"));
        if (cardEvening != null) cardEvening.setOnClickListener(v -> selectPreset("evening"));

        if (rbMorning != null) rbMorning.setOnClickListener(v -> selectPreset("morning"));
        if (rbAfternoon != null) rbAfternoon.setOnClickListener(v -> selectPreset("afternoon"));
        if (rbEvening != null) rbEvening.setOnClickListener(v -> selectPreset("evening"));

        if (etCustomTime != null) {
            etCustomTime.setOnClickListener(v -> showTimePicker());
        }

        if (btnGetStarted != null) {
            btnGetStarted.setOnClickListener(v -> {
                prefManager.setString("reminder_time", selectedReminder);
                if (getActivity() instanceof OnBoardingScreen) {
                    ((OnBoardingScreen) getActivity()).finishOnboarding();
                }
            });
        }

        if (tvSkipSetup != null) {
            tvSkipSetup.setOnClickListener(v -> {
                if (getActivity() instanceof OnBoardingScreen) {
                    ((OnBoardingScreen) getActivity()).finishOnboarding();
                }
            });
        }
    }

    private void selectPreset(String type) {
        if (rbMorning != null) rbMorning.setChecked(false);
        if (rbAfternoon != null) rbAfternoon.setChecked(false);
        if (rbEvening != null) rbEvening.setChecked(false);

        if (cardMorning != null) cardMorning.setBackgroundResource(R.drawable.bg_reminder_unselected);
        if (cardAfternoon != null) cardAfternoon.setBackgroundResource(R.drawable.bg_reminder_unselected);
        if (cardEvening != null) cardEvening.setBackgroundResource(R.drawable.bg_reminder_unselected);

        if (etCustomTime != null) {
            etCustomTime.setText("");
        }

        switch (type) {
            case "morning":
                if (rbMorning != null) rbMorning.setChecked(true);
                if (cardMorning != null) cardMorning.setBackgroundResource(R.drawable.bg_reminder_selected);
                selectedReminder = "7:30 AM";
                break;
            case "afternoon":
                if (rbAfternoon != null) rbAfternoon.setChecked(true);
                if (cardAfternoon != null) cardAfternoon.setBackgroundResource(R.drawable.bg_reminder_selected);
                selectedReminder = "1:00 PM";
                break;
            case "evening":
                if (rbEvening != null) rbEvening.setChecked(true);
                if (cardEvening != null) cardEvening.setBackgroundResource(R.drawable.bg_reminder_selected);
                selectedReminder = "8:00 PM";
                break;
        }
    }

    private void showTimePicker() {
        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(8)
                .setMinute(0)
                .setTitleText("Select Reminder Time")
                .build();

        picker.addOnPositiveButtonClickListener(dialog -> {
            int hour = picker.getHour();
            int minute = picker.getMinute();
            selectedReminder = formatTime(hour, minute);
            if (etCustomTime != null) {
                etCustomTime.setText(selectedReminder);
            }

            // Uncheck presets
            if (rbMorning != null) rbMorning.setChecked(false);
            if (rbAfternoon != null) rbAfternoon.setChecked(false);
            if (rbEvening != null) rbEvening.setChecked(false);

            if (cardMorning != null) cardMorning.setBackgroundResource(R.drawable.bg_reminder_unselected);
            if (cardAfternoon != null) cardAfternoon.setBackgroundResource(R.drawable.bg_reminder_unselected);
            if (cardEvening != null) cardEvening.setBackgroundResource(R.drawable.bg_reminder_unselected);
        });

        picker.show(getParentFragmentManager(), "time_picker");
    }

    private String formatTime(int hour, int minute) {
        String amPm = hour >= 12 ? "PM" : "AM";
        int rem = hour % 12;
        int hour12 = (rem == 0) ? 12 : rem;
        String formattedMinute = minute < 10 ? "0" + minute : "" + minute;
        return String.format(Locale.getDefault(), "%d:%s %s", hour12, formattedMinute, amPm);
    }
}
