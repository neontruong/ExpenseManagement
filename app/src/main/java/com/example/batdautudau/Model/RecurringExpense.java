package com.example.batdautudau.Model;

public class RecurringExpense {
    private int id; // Unique identifier for the expense
    private String description; // Description of the expense
    private double amount; // Amount for the recurring expense
    private String category; // Category of the expense
    private String startDate; // Start date of the recurring expense
    private String endDate; // End date of the recurring expense

    // Default constructor
    public RecurringExpense() {}

    // Constructor without ID (for new records before saving to the database)
    public RecurringExpense(String description, double amount,
                            String category, String startDate, String endDate) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Constructor with ID (for existing records retrieved from the database)
    public RecurringExpense(int id, String description, double amount,
                            String category, String startDate, String endDate) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return description + " - " + amount + " VND\n" +

                "Start: " + startDate + ", End: " + endDate;
    }
}
