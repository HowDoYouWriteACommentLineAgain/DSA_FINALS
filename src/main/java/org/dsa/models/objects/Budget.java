package org.dsa.models.objects;

import org.dsa.models.enums.BudgetCategory;
import java.time.LocalDate;

public class Budget {
    private int id;
    private int userId;
    private BudgetCategory cat;
    private float limit;
    private float spent;
    private LocalDate timeLimit;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BudgetCategory getCat() {
        return cat;
    }

    public void setCat(BudgetCategory cat) {
        this.cat = cat;
    }

    public float getLimit() {
        return limit;
    }

    public void setLimit(float limit) {
        this.limit = limit;
    }

    public float getSpent() {
        return spent;
    }

    public void setSpent(float spent) {
        this.spent = spent;
    }

    public LocalDate getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(LocalDate timeLimit) {
        this.timeLimit = timeLimit;
    }

    Budget(){};
}
