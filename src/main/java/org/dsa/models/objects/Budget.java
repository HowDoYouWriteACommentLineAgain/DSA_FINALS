package org.dsa.models.objects;

import org.dsa.abstractions.objectModel;

import java.sql.Date;

public record Budget (int id,  int expense_cat, double max_amount, double goal_amount, Date start_date, Date end_date) implements objectModel{
    @Override
    public boolean validate() {
        return (expense_cat != 0 && goal_amount > 0 && max_amount > 0);
    }
}
