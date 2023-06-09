package com.codecool.ehotel.service.buffet;

import com.codecool.ehotel.model.Buffet;
import com.codecool.ehotel.model.Guest;
import com.codecool.ehotel.model.MealType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface BuffetService {
    Buffet addMeal(Buffet buffet, MealType mealType, LocalDateTime timeStamp, int amount);
    boolean consumeFreshest(Guest guest, List<MealType> mealPreferences, Buffet buffet);
    Buffet refillBuffet(Buffet buffet);
    Buffet collectWaste(Buffet buffet);
    Guest makeGuestUnhappy(Guest guest); // TODO maybe extract from this service
    Buffet createBuffet();
}

