package com.codecool.ehotel.service.buffet;

import com.codecool.ehotel.model.*;
import com.codecool.ehotel.service.logger.ConsoleLogger;
import com.codecool.ehotel.service.logger.Logger;

import java.time.LocalDateTime;
import java.util.*;

public class BuffetServiceImpl implements BuffetService {
    private final Logger logger;

    private Meal meal;

    private int cycle;

    public BuffetServiceImpl() {
        this.logger = new ConsoleLogger();
    }

    private void createMeals(MealType mealType, LocalDateTime timestamp, int amount, PriorityQueue<Meal> meals) {
        MealDurability mealDurability = mealType.getDurability();
        for (int i = 0; i < amount; i++) {
            meal = new Meal(mealType, timestamp.plusDays(mealDurability.ordinal()), cycle);
            meals.add(meal);
        }
    }

    //TODO: Split the check and the log function
    private boolean consumeMealAndLog(Guest guest, MealType mealType, PriorityQueue<Meal> meals) {
        List<Meal> mealsOfType = new ArrayList<>(meals);
        mealsOfType.removeIf(meal -> !meal.getMealType().equals(mealType));

        if (!mealsOfType.isEmpty()) {
            //mealsOfType.sort(Comparator.comparing(meal -> meal.getTimestamp()));
            meals.remove(mealsOfType.get(0));
            logger.logInfo(guest.name() + " consumed " + mealType);
            return true;
        } else {
            logger.logInfo("No meals of type " + mealType + " available for " + guest.name());
            return false;
        }
    }

    @Override
    public Buffet createBuffet() {
        int amount = 5; // TODO: Export to interface as constant

        Map<MealType, PriorityQueue<Meal>> meals = initializeBuffet();

        Buffet buffet = new Buffet(meals);
        for (MealType mealType : MealType.values()) {
            buffet = addMeal(buffet, mealType, LocalDateTime.now(), amount);
        }

        logger.logInfo("Buffet created successfully with " + amount + " meals of each type.");
        return buffet;
    }

    private static Map<MealType, PriorityQueue<Meal>> initializeBuffet() {
        Map<MealType, PriorityQueue<Meal>> meals = new HashMap<>();
        for (MealType mealType : MealType.values()) {
            meals.put(mealType, new PriorityQueue<>());
        }
        return meals;
    }

    @Override
    public boolean consumeFreshest(Guest guest, List<MealType> mealPreferences, Buffet buffet) {
        Random random = new Random(); // TODO: Inject through constructor
        MealType randomMealType = mealPreferences.get(random.nextInt(mealPreferences.size()));
        PriorityQueue<Meal> meals = buffet.searchMealType(randomMealType);
        boolean consumed = consumeMealAndLog(guest, randomMealType, meals);
        if (!consumed) {
            makeGuestUnhappy(guest);
        }
        return consumed;
    }

    @Override
    public Buffet addMeal(Buffet buffet, MealType mealType, LocalDateTime timestamp, int amount) {
        cycle = 0;
        Map<MealType, PriorityQueue<Meal>> mutableMeals = new HashMap<>(buffet.meals());

        PriorityQueue<Meal> meals = mutableMeals.getOrDefault(mealType, new PriorityQueue<>());
        createMeals(mealType, timestamp, amount, meals);

        mutableMeals.put(mealType, meals);
        return new Buffet(Map.copyOf(mutableMeals)); // TODO: set map of the buffet rather than returning it
    }

    @Override
    public Buffet refillBuffet(Buffet buffet) {
        cycle++;
        for (MealType mealType : MealType.values()) {
            PriorityQueue<Meal> meals = buffet.searchMealType(mealType);
            if (meals.size() < 5) {
                createMeals(mealType, LocalDateTime.now(), 5 - meals.size(), meals);
                logger.logInfo(mealType + " refilled to " + meals.size() + " cycle: " + meal.getCycle());
            }
        }
        return buffet;
    }

    @Override
    public void collectWaste(Buffet buffet) {
        for (MealType mealType : MealType.values()) {
            PriorityQueue<Meal> meals = buffet.searchMealType(mealType);
            int originalSize = meals.size();

            if(cycle == 8) {
                meals.removeIf(meal -> meal.getMealType().getDurability() == MealDurability.SHORT
                        || meal.getMealType().getDurability() == MealDurability.MEDIUM);
            } else {
                meals.removeIf(meal -> meal.getMealType().getDurability() == MealDurability.SHORT && meal.getCycle() == cycle - 3);
            }
            logger.logInfo("Collected " + mealsRemoved + " waste meals of type " + mealType + " with cycle " + cycle);
        }

    }

    @Override
    public Guest makeGuestUnhappy(Guest guest) {
        return new Guest(guest.name(), guest.guestType(), guest.checkIn(), guest.checkOut(), guest.stayDuration(), false);
    }

}
