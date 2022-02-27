package com.example.expensearo.JavaClasses;

public class MonthlyBudget
{
    String amount,date,note,type,category,icon,time;
    public MonthlyBudget(){}
    public MonthlyBudget(String amount, String date, String note, String type, String category,String icon, String time) {
        this.amount = amount;
        this.date = date;
        this.note = note;
        this.type = type;
        this.category=category;
        this.icon=icon;
        this.time=time;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
