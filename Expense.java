package model;

public class Expense {

    private int id;
    private double amount;
    private String category;
    private String description;
    private String date;
    private String paymentMethod;

    public Expense(double amount, String category, String description,
                   String date, String paymentMethod) {

        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
        this.paymentMethod = paymentMethod;
    }

    public Expense(int id, double amount, String category,
                   String description, String date,
                   String paymentMethod) {

        this.id = id;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
        this.paymentMethod = paymentMethod;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}