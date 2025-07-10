package org.dsa.models.objects;

import org.dsa.abstractions.objectModel;

import java.sql.Date;

public record Expense (
        int id,
        String name,
        int expense_cat,
        double amount,
        String note,
        Date date) implements objectModel {
    @Override
    public boolean validate() {
        return (expense_cat != 0 && amount > 0) ;
    }
}
