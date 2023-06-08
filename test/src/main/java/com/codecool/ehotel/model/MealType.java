package com.codecool.ehotel.model;

import java.util.Objects;

import static com.codecool.ehotel.model.MealDurability.*;

public enum MealType {
    SCRAMBLED_EGGS (70, SHORT),
    SUNNY_SIDE_UP (70, SHORT),
    FRIED_SAUSAGE(100, SHORT),
    FRIED_BACON(70, SHORT),
    PANCAKE(40, SHORT),
    CROISSANT(40, SHORT),
    MASHED_POTATO(20, MEDIUM),
    MUFFIN(20, MEDIUM),
    BUN(10, MEDIUM),
    CEREAL(30, LONG),
    MILK(10, LONG);

    private int cost;
    private MealDurability mealDurability;

    MealType(int cost, MealDurability mealDurability) {
        this.cost = cost;
        this.mealDurability = mealDurability;
    }

    public int getCost() {
        return cost;
    }

    public MealDurability getDurability() {
        return mealDurability;
    }

}
