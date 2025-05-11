package com.example.batdautudau.Model;

public class Budget {
    private int id;
    private String category;
    private int maxBudget;
    private String startDate;
    private String endDate;

    public Budget(String category, int maxBudget, String startDate, String endDate) {
        this.category = category;
        this.maxBudget = maxBudget;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getMaxBudget() { return maxBudget; }
    public void setMaxBudget(int maxBudget) { this.maxBudget = maxBudget; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
}
