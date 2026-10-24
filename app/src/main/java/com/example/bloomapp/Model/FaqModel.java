package com.example.bloomapp.Model;

public class FaqModel {
    private final String title;
    private final String description;

    public FaqModel(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}