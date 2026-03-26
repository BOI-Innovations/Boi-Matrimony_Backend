package com.matrimony.model.dto.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.matrimony.model.entity.Profile;

public class MatchResult {
    private Profile profile;
    private double matchPercentage;
    private List<String> matchReasons;
    private Map<String, Double> categoryScores;

    public MatchResult() {}

    public MatchResult(Profile profile, double matchPercentage, List<String> matchReasons) {
        this.profile = profile;
        this.matchPercentage = matchPercentage;
        this.matchReasons = matchReasons;
        this.categoryScores = new HashMap<>();
    }

    public MatchResult(Profile profile, double matchPercentage, List<String> matchReasons, Map<String, Double> categoryScores) {
        this.profile = profile;
        this.matchPercentage = matchPercentage;
        this.matchReasons = matchReasons;
        this.categoryScores = categoryScores;
    }

    // Getters and Setters
    public Profile getProfile() { return profile; }
    public void setProfile(Profile profile) { this.profile = profile; }

    public double getMatchPercentage() { return matchPercentage; }
    public void setMatchPercentage(double matchPercentage) { this.matchPercentage = matchPercentage; }

    public List<String> getMatchReasons() { return matchReasons; }
    public void setMatchReasons(List<String> matchReasons) { this.matchReasons = matchReasons; }

    public Map<String, Double> getCategoryScores() { return categoryScores; }
    public void setCategoryScores(Map<String, Double> categoryScores) { this.categoryScores = categoryScores; }

    @Override
    public String toString() {
        return "MatchResult{" +
                "profile=" + profile.getId() +
                ", matchPercentage=" + matchPercentage +
                ", matchReasons=" + matchReasons +
                ", categoryScores=" + categoryScores +
                '}';
    }
}