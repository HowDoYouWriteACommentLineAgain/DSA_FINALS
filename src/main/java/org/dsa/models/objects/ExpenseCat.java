package org.dsa.models.objects;

import org.dsa.abstractions.ObjectModel;

public record ExpenseCat(int id, String name) implements ObjectModel {
    public boolean validate()
    {
        return (!name.isEmpty());
    }
}
