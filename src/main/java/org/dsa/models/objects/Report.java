package org.dsa.models.objects;

public record Report (int period, double totalIncome,double totalExpense, double netSavings, double largestExpense) {
    public double netSavings()
    {
        return totalIncome- largestExpense;
    }
}

