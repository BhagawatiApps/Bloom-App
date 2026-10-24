package com.example.bloomapp.OnBoarding.OnBoardingModels;

public class HabitListModel {
    String habitId;
    String HabitTitle;
    String HabitDescription;
    String HabitLogo;
    Boolean HabitState;

    public HabitListModel(String habitId, String habitTitle, String habitDescription, String habitLogo, Boolean habitState) {
        this.habitId = habitId;
        HabitTitle = habitTitle;
        HabitDescription = habitDescription;
        HabitLogo = habitLogo;
        HabitState = habitState;
    }

    public String getHabitId() {
        return habitId;
    }

    public void setHabitId(String habitId) {
        this.habitId = habitId;
    }

    public String getHabitTitle() {
        return HabitTitle;
    }

    public void setHabitTitle(String habitTitle) {
        HabitTitle = habitTitle;
    }

    public String getHabitDescription() {
        return HabitDescription;
    }

    public void setHabitDescription(String habitDescription) {
        HabitDescription = habitDescription;
    }

    public String getHabitLogo() {
        return HabitLogo;
    }

    public void setHabitLogo(String habitLogo) {
        HabitLogo = habitLogo;
    }

    public Boolean getHabitState() {
        return HabitState;
    }

    public void setHabitState(Boolean habitState) {
        HabitState = habitState;
    }
}
