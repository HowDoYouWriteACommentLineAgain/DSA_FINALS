package org.dsa.models.objects;

import java.time.LocalDate;

public class Transaction {
    private int id;
    private String name;
    private int UserId;
    private String t_type; //Expense or Income
    private int refId;
    private double amount;
    private String note;
    private LocalDate t_date;

    public Transaction(){}

    public boolean validate()
    {
        if (t_type == null || (!t_type.equals("income") && !t_type.equals("expense"))) return false;
        if (amount <= 0) return false;
        if (t_date == null) return false;
        return true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getT_type() {
        return t_type;
    }

    public void setT_type(String t_type) {
        this.t_type = t_type;
    }

    public int getRefId() {
        return refId;
    }

    public void setRefId(int refId) {
        this.refId = refId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDate getT_date() {
        return t_date;
    }

    public void setT_date(LocalDate t_date) {
        this.t_date = t_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
