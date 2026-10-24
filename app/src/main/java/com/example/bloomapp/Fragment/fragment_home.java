package com.example.bloomapp.Fragment;

import static android.view.View.GONE;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.bloomapp.Adapter.DailyProgressAdapter;
import com.example.bloomapp.Adapter.FaqAdapter;
import com.example.bloomapp.Data.Entity.HabitEntity;
import com.example.bloomapp.Model.FaqModel;
import com.example.bloomapp.OnBoarding.OnBoardingModels.HabitListModel;
import com.example.bloomapp.R;
import com.example.bloomapp.Utils.HabitManager;
import com.example.bloomapp.Utils.SharedPrefManager;
import com.example.bloomapp.ads.AdManager;
import com.google.android.gms.ads.AdView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class fragment_home extends Fragment {

    private TextView tvGoalsDate;
    private TextView tvProgressStatus;
    private LinearProgressIndicator progressGoals;
    private MaterialButton btnMarkDone, btnRemindLater;
    private LinearLayout llDailyTips;
    private View cardTodaysTip;
    private TextView tvTodaysTipLabel;

    private SharedPrefManager prefManager;

    private ViewPager2 faqViewPager;
    private LinearLayout faqIndicators;
    private FaqAdapter faqAdapter;

    private LinearLayout untrackedHabitsContainer;
    private RecyclerView rvDailyProgressList;
    private DailyProgressAdapter dailyProgressAdapter;


    private Handler faqHandler;
    private Runnable faqRunnable;

    private ViewPager2.OnPageChangeCallback faqPageCallback;

    private final List<FaqModel> faqList = new ArrayList<>();

    public fragment_home() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(
                R.layout.fragment_home,
                container,
                false
        );

        prefManager = SharedPrefManager.getInstance(requireContext());

        initView(view);
        setCurrentDate();
        setupFaqData();
        setupFaqCarousel();
        loadUntrackedHabits();
        loadDailyProgressHabits();
        setupDailyTipsCard();
        updateProgress();

        return view;
    }



    private void setupDailyTipsCard() {
        if (!isAdded()) return;

        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String completedDate = prefManager.getDailyTipCompletedDate();

        boolean isCompletedToday = today.equals(completedDate) || HabitManager.isHabitCompletedToday(requireContext(), "sleep");

        if (isCompletedToday) {
            setTipCardVisible(false);
        } else {
            setTipCardVisible(true);
        }

        if (btnRemindLater != null) {
            btnRemindLater.setOnClickListener(v -> setTipCardVisible(false));
        }

        if (btnMarkDone != null) {
            btnMarkDone.setOnClickListener(v -> {
                prefManager.setDailyTipCompletedDate(today);
                HabitManager.updateHabitCompletion(requireContext(), "sleep", true);
                setTipCardVisible(false);
                updateProgress();
                loadDailyProgressHabits();
            });
        }
    }

    private void setTipCardVisible(boolean visible) {
        int visibility = visible ? View.VISIBLE : GONE;
        if (cardTodaysTip != null) {
            cardTodaysTip.setVisibility(visibility);
        } else if (llDailyTips != null) {
            llDailyTips.setVisibility(visibility);
        }
        if (tvTodaysTipLabel != null) {
            tvTodaysTipLabel.setVisibility(visibility);
        }
    }

    private void initView(View view) {
        tvGoalsDate = view.findViewById(R.id.tvGoalsDate);
        tvProgressStatus = view.findViewById(R.id.tvProgressStatus);
        progressGoals = view.findViewById(R.id.progressGoals);
        btnMarkDone = view.findViewById(R.id.btnMarkDone);
        btnRemindLater = view.findViewById(R.id.btnRemindLater);
        llDailyTips = view.findViewById(R.id.llDailyTips);
        cardTodaysTip = view.findViewById(R.id.cardTodaysTip);
        tvTodaysTipLabel = view.findViewById(R.id.tvTodaysTipLabel);
        faqViewPager = view.findViewById(R.id.faqViewPager);
        faqIndicators = view.findViewById(R.id.faqIndicators);
        untrackedHabitsContainer = view.findViewById(R.id.untrackedHabitsContainer);
        rvDailyProgressList = view.findViewById(R.id.rvDailyProgressList);

        if (rvDailyProgressList != null) {
            rvDailyProgressList.setLayoutManager(new LinearLayoutManager(requireContext()));
            rvDailyProgressList.setNestedScrollingEnabled(false);
        }
    }

    private void setCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEEE, MMMM d",
                Locale.ENGLISH
        );

        tvGoalsDate.setText(dateFormat.format(new Date()));
    }

    private void setupFaqData() {
        faqList.clear();

        faqList.add(new FaqModel(
                "What exactly is PCOS?",
                "PCOS stands for polycystic ovary syndrome..."
        ));

        faqList.add(new FaqModel(
                "What are common PCOS symptoms?",
                "Symptoms can vary from person to person..."
        ));

        faqList.add(new FaqModel(
                "Can lifestyle changes help?",
                "Healthy daily habits may help manage symptoms..."
        ));

        faqList.add(new FaqModel(
                "How is PCOS diagnosed?",
                "A healthcare professional can evaluate your symptoms..."
        ));

        faqList.add(new FaqModel(
                "Can PCOS affect periods?",
                "PCOS may cause irregular or missed periods..."
        ));
    }

    private void setupFaqCarousel() {
        faqHandler = new Handler(Looper.getMainLooper());

        faqAdapter = new FaqAdapter(faqList);
        faqViewPager.setAdapter(faqAdapter);

        createFaqIndicators();

        faqPageCallback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateFaqIndicators(position);
            }
        };

        faqViewPager.registerOnPageChangeCallback(faqPageCallback);

        faqRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isAdded() || faqViewPager == null) {
                    return;
                }

                int count = faqList.size();

                if (count > 1) {
                    int nextPosition =
                            (faqViewPager.getCurrentItem() + 1) % count;

                    faqViewPager.setCurrentItem(nextPosition, true);

                    faqHandler.postDelayed(this, 3000);
                }
            }
        };
    }

    private void createFaqIndicators() {
        faqIndicators.removeAllViews();

        for (int i = 0; i < faqList.size(); i++) {
            View dot = new View(requireContext());

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            dp(i == 0 ? 20 : 7),
                            dp(7)
                    );

            params.setMargins(dp(3), 0, dp(3), 0);

            dot.setLayoutParams(params);
            dot.setBackgroundResource(
                    i == 0
                            ? R.drawable.bg_faq_indicator_selected
                            : R.drawable.bg_faq_indicator
            );

            faqIndicators.addView(dot);
        }
    }

    private void updateFaqIndicators(int selectedPosition) {
        if (faqIndicators == null) {
            return;
        }

        for (int i = 0; i < faqIndicators.getChildCount(); i++) {
            View dot = faqIndicators.getChildAt(i);

            LinearLayout.LayoutParams params =
                    (LinearLayout.LayoutParams) dot.getLayoutParams();

            params.width = dp(i == selectedPosition ? 20 : 7);
            dot.setLayoutParams(params);

            dot.setBackgroundResource(
                    i == selectedPosition
                            ? R.drawable.bg_faq_indicator_selected
                            : R.drawable.bg_faq_indicator
            );
        }
    }

    private int dp(int value) {
        return (int) (value * getResources()
                .getDisplayMetrics().density + 0.5f);
    }

    private void updateProgress() {
        if (!isAdded()) {
            return;
        }

        int completed = HabitManager.getCompletedHabitsCount(
                requireContext()
        );

        int total = HabitManager.getEnableHabitsCount(
                requireContext()
        );

        if (progressGoals != null) {
            progressGoals.setMax(Math.max(total, 1));
            progressGoals.setProgress(
                    Math.min(completed, Math.max(total, 1))
            );
        }

        if (tvProgressStatus != null) {
            tvProgressStatus.setText(
                    completed + " of " + total + " complete"
            );
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        updateProgress();
        setCurrentDate();
        loadUntrackedHabits();
        loadDailyProgressHabits();
        setupDailyTipsCard();

        if (faqHandler != null && faqRunnable != null) {
            faqHandler.removeCallbacks(faqRunnable);
            faqHandler.postDelayed(faqRunnable, 3000);
        }

    }

    private void loadDailyProgressHabits() {
        if (!isAdded() || rvDailyProgressList == null) return;

        List<HabitEntity> enabledHabits = HabitManager.getEnabledHabits(requireContext());
        if (dailyProgressAdapter == null) {
            dailyProgressAdapter = new DailyProgressAdapter(requireContext(), enabledHabits, habit -> {
                // Do not update any data on click daily habit tracking
            });
            rvDailyProgressList.setAdapter(dailyProgressAdapter);
        } else {
            dailyProgressAdapter.updateList(enabledHabits);
        }
    }

    private void loadUntrackedHabits() {
        if (!isAdded() || untrackedHabitsContainer == null) return;

        untrackedHabitsContainer.removeAllViews();
        ArrayList<HabitListModel> untrackedList = HabitManager.getUntrackedHabits(requireContext());

        LayoutInflater inflater = LayoutInflater.from(requireContext());
        int limit = Math.min(2, untrackedList.size());
        for (int i = 0; i < limit; i++) {
            HabitListModel model = untrackedList.get(i);
            View itemView = inflater.inflate(R.layout.item_untracked_habit, untrackedHabitsContainer, false);

            ImageView ivIcon = itemView.findViewById(R.id.ivUntrackedIcon);
            TextView tvTitle = itemView.findViewById(R.id.tvUntrackedTitle);
            TextView tvDesc = itemView.findViewById(R.id.tvUntrackedDescription);

            String fullTitle = model.getHabitTitle();
            String mainTitle = fullTitle;
            String subtitle = model.getHabitDescription();

            if (fullTitle != null && fullTitle.contains("·")) {
                String[] parts = fullTitle.split("·");
                mainTitle = parts[0].trim();
                if (parts.length > 1) {
                    subtitle = parts[1].trim();
                }
            }

            tvTitle.setText(mainTitle);
            tvDesc.setText(subtitle);
            loadHabitIcon(ivIcon, model.getHabitLogo());

            // Do not update any data on click untracked habit list

            untrackedHabitsContainer.addView(itemView);
        }
    }

    private void loadHabitIcon(ImageView imageView, String logoStr) {
        if (logoStr != null && !logoStr.trim().isEmpty()) {
            try {
                int resId = Integer.parseInt(logoStr);
                Glide.with(this)
                        .load(resId)
                        .placeholder(R.drawable.ic_meals)
                        .error(R.drawable.ic_meals)
                        .into(imageView);
                return;
            } catch (NumberFormatException ignored) {
            }

            int resId = getResources().getIdentifier(logoStr, "drawable", requireContext().getPackageName());
            if (resId != 0) {
                Glide.with(this)
                        .load(resId)
                        .placeholder(R.drawable.ic_meals)
                        .error(R.drawable.ic_meals)
                        .into(imageView);
                return;
            }

            Glide.with(this)
                    .load(logoStr)
                    .placeholder(R.drawable.ic_meals)
                    .error(R.drawable.ic_meals)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.ic_meals);
        }
    }

    @Override
    public void onPause() {
        stopFaqAutoScroll();
        super.onPause();
    }

    private void stopFaqAutoScroll() {
        if (faqHandler != null && faqRunnable != null) {
            faqHandler.removeCallbacks(faqRunnable);
        }
    }

    @Override
    public void onDestroyView() {
        stopFaqAutoScroll();


        if (faqViewPager != null && faqPageCallback != null) {
            faqViewPager.unregisterOnPageChangeCallback(
                    faqPageCallback
            );
        }

        faqViewPager = null;
        faqIndicators = null;
        untrackedHabitsContainer = null;
        rvDailyProgressList = null;
        dailyProgressAdapter = null;
        tvGoalsDate = null;
        tvProgressStatus = null;
        progressGoals = null;
        btnMarkDone = null;
        btnRemindLater = null;
        llDailyTips = null;
        cardTodaysTip = null;
        tvTodaysTipLabel = null;

        super.onDestroyView();
    }
}
