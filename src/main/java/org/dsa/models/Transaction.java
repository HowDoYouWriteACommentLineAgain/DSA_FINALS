package org.dsa.models;

import org.dsa.enums.Category;
import org.dsa.enums.Type;

import java.sql.Date;

public class Transaction
{
    private int id;
    private final float amount;
    private final Type type;
    private final Category cat;
    private final Date date;
    private final String note;


    public Transaction(float amount, Type type, Category cat, Date date){
        this(amount, type, cat, date, "");
    }

    public Transaction(float amount, Type type, Category cat, Date date, String note)
    {
        this.amount = amount;
        this.type = type;
        this.cat = cat;
        this.date = date;
        this.note = note;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return this.id;
    }

    public float getAmount()
    {
        return this.amount;
    }

    public String getType()
    {
        return this.type.name();
    }

    public String getCat()
    {
        return this.cat.name();
    }

    public Date getDate()
    {
        return this.date;
    }

    public String getNote()
    {
        return this.note;
    }
}


