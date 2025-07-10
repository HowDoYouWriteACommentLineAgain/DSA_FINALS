package org.dsa.models.objects;

import org.dsa.abstractions.objectModel;

public record IncomeCat(int id, String name) implements objectModel {
    public boolean validate()
    {
        return (!name.isEmpty());
    }
}