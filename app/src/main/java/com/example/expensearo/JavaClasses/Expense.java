package com.example.expensearo.JavaClasses;

public class Expense
{
    String Icon,Name;
    public Expense(){}

    public Expense(String icon, String name) {
        Icon = icon;
        Name = name;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
