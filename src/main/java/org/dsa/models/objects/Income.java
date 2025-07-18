package org.dsa.models.objects;

import org.dsa.abstractions.ObjectModel;

import java.sql.Date;

public record Income (
        int id,
        String name,
        int income_cat,
        double amount,
        String note,
        Date date
) implements ObjectModel {

    @Override
    public boolean validate() {
        return (income_cat != 0 && amount > 0) ;
    }
}

