package com.codecool.ehotel.model;

import java.util.Map;
import java.util.PriorityQueue;

public record Buffet(Map<MealType, PriorityQueue<Meal>> meals) {
    public PriorityQueue<Meal> searchMealType(MealType type) {
        return meals.getOrDefault(type, new PriorityQueue<>());
    }
}
