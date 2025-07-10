package org.dsa.models.objects;

import org.dsa.abstractions.objectModel;

import java.sql.Date;

public record Budget (int id,  int expense_cat, double max_amount, double goal_amount, Date start_date, Date end_date) implements objectModel{
    @Override
    public boolean validate() {
        return false;
    }
}
