package com.codecool.ehotel.service;

import com.codecool.ehotel.model.Buffet;
import com.codecool.ehotel.model.Guest;
import com.codecool.ehotel.model.Meal;
import com.codecool.ehotel.model.MealType;
import com.codecool.ehotel.service.buffet.BuffetService;
import com.codecool.ehotel.service.logger.Logger;

import java.util.*;

public class BreakfastManager {
    private final Logger logger;
    private final BuffetService buffetService;

    public BreakfastManager(Logger logger, BuffetService buffetService) {
        this.logger = logger;
        this.buffetService = buffetService;
    }

    public void serveBreakfast(List<Set<Guest>> guestGroups, Buffet buffet) {
        int groupNumber = 1;
        int happyGuests = 0;
        int unhappyGuests = 0;


        for (int i = 0; i < guestGroups.size(); i++) {
            Set<Guest> group = guestGroups.get(i);

            if (group.isEmpty()) {
                logger.logInfo("\nGroup " + groupNumber + " has no guests. Skipping...");
                groupNumber++;
                continue;
            }

            Set<Guest> newGroup = new HashSet<>();

            for (Guest guest : group) {
                List<MealType> mealPreferences = guest.guestType().getMealPreferences();
                if(!buffetService.consumeFreshest(guest, mealPreferences, buffet)) {
                    newGroup.add(buffetService.makeGuestUnhappy(guest));
                    unhappyGuests++;
                } else {
                    newGroup.add(guest);
                    happyGuests++;
                }
            }

            guestGroups.set(i, newGroup);

            logger.logInfo("\nRefilling Buffet:");
            buffet = buffetService.refillBuffet(buffet);

            logger.logInfo("\nCollecting waste:");
            buffetService.collectWaste(buffet);

            printGroupInfo(guestGroups, buffet, groupNumber);

            if (groupNumber == 8) {
                logger.logInfo("Number of happy guests: " + happyGuests);
                logger.logInfo("Number of unhappy guests: " + unhappyGuests);
            }

            groupNumber++;

            logger.logInfo("\n------------------------------------------------------\n");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void printGroupInfo(List<Set<Guest>> guestGroups, Buffet buffet, int groupNumber) {
        if (groupNumber == guestGroups.size()) {
            for (MealType mealType : MealType.values()) {
                PriorityQueue<Meal> meals = buffet.searchMealType(mealType);
                logger.logInfo("After last cycle, " + meals.size() + " " + mealType + " meals remain, each with cycle: ");
                for (Meal meal : meals) {
                    logger.logInfo("  " + meal.getCycle());
                }
            }
        }
    }
}
