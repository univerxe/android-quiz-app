package com.example.app_project;

public class Vocab {
    private final String korean;
    private final String english;

    public Vocab(String korean, String english) {
        this.korean = korean;
        this.english = english;
    }

    public String getKorean() {
        return korean;
    }

    public String getEnglish() {
        return english;
    }
}
