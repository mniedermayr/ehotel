package com.codecool.ehotel.model;

import java.time.LocalDateTime;

public class Meal implements Comparable<Meal> {
    private MealType mealType;
    private LocalDateTime timestamp;
    private int cycle;

    public Meal(MealType mealType, LocalDateTime timestamp, int cycle) {
        this.mealType = mealType;
        this.timestamp = timestamp;
        this.cycle = cycle;
    }

    public MealType getMealType() {
        return this.mealType;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public int getCycle() {
        return this.cycle;
    }

    @Override
    public int compareTo(Meal other) {
        return this.timestamp.compareTo(other.timestamp);
    }

    @Override
    public String toString() {
        return mealType + " - " + mealType.getDurability() + " - " + timestamp + " - Cycle: " + cycle;
    }
}
