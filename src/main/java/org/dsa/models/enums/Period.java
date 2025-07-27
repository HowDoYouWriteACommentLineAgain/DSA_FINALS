package org.dsa.models.enums;

public enum Period {
    DAILY("Daily", true),
    WEEKLY("Weekly", true),
    FORTNIGHTLY("Fortnightly (14d)", true),
    _30DAYS("30 Days", true),
    _365DAYS("1 Year (365d)", false),
    _5YEARS("5 Years (365d)", false),
    _10YEARS("10 Years (365d)", false);

    private boolean isTooSpecific;
    private final String label;

    Period(String label, boolean isTooSpecific) {
        this.label = label;
        this.isTooSpecific = isTooSpecific;
    }


    public boolean isTooSpecific() {
        return isTooSpecific;
    }

    @Override
    public String toString() {
        return label;
    }
}