package com.codecool.ehotel;

import com.codecool.ehotel.model.Buffet;
import com.codecool.ehotel.model.Guest;
import com.codecool.ehotel.model.Meal;
import com.codecool.ehotel.model.MealType;
import com.codecool.ehotel.service.BreakfastManager;
import com.codecool.ehotel.service.buffet.BuffetService;
import com.codecool.ehotel.service.guest.GuestService;
import com.codecool.ehotel.service.logger.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class ApplicationInterface {
    private final Logger logger;
    private final Scanner scanner;
    private final BuffetService buffetService;
    private final GuestService guestService;
    private final BreakfastManager breakfastManager;

    public ApplicationInterface(Logger logger, BuffetService buffetService, GuestService guestService, Scanner scanner, BreakfastManager breakfastManager) {
        this.logger = logger;
        this.buffetService = buffetService;
        this.guestService = guestService;
        this.scanner = scanner;
        this.breakfastManager = breakfastManager;
    }

    public void run() {
        Buffet buffet = buffetService.createBuffet();
        LocalDate today = LocalDate.now();
        List<Guest> guests = new ArrayList<>();
        boolean running = true;

        LocalDate seasonStart = LocalDate.now().minusMonths(1);
        LocalDate seasonEnd = LocalDate.now().plusMonths(1);
        for (int i = 0; i < 200; i++) {
            guests.add(guestService.generateRandomGuest(seasonStart, seasonEnd));
        }

        List<Set<Guest>> guestGroups = guestService.divideGuestsIntoGroups(guests, today);

        while (running) {
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {
                    if (buffet != null) {
                        searchForMealType(buffet);
                    } else {
                        logger.logError("Buffet is not created yet. Please create a buffet first.");
                    }
                }
                case "3" -> {
                    logGroupInfo(guestGroups);
                }
                case "4" -> printGuestsForUserInputDate(guests);
                case "5" -> breakfastManager.serveBreakfast(guestGroups, buffet);
                case "6" -> running = false;
                default -> logger.logError("Invalid choice. Please try again.");
            }
        }
    }

    private void logGroupInfo(List<Set<Guest>> guestGroups) {
        for (int i = 0; i < guestGroups.size(); i++) {
            Set<Guest> group = guestGroups.get(i);

            logger.logInfo("Group " + (i + 1) + ":");
            for (Guest guest : group) {
                logger.logInfo(guest.name() + " - " + guest.guestType());
            }
        }
    }

    private void printMenu() {
        System.out.println("Please choose an option:");
        System.out.println("1. Search for a meal by type");
        System.out.println("3. Print guest groups for today");
        System.out.println("4. Print guests for a date");
        System.out.println("5. Serve Breakfast");
        System.out.println("6. Exit");
    }

    private void guestsForDay(List<Guest> guests, LocalDate date) {
        Set<Guest> guestsForDay = guestService.getGuestsForDay(guests, date);

        if (!guestsForDay.isEmpty()) {
            logger.logInfo("Guests for " + date + ":");
            for (Guest guest : guestsForDay) {
                logger.logInfo(guest.name() + " - " + guest.guestType() + " - stays: " + guest.stayDuration() + " - checkout: " + guest.checkOut());
            }
        } else {
            logger.logInfo("No guests found for " + date);
        }
    }

    private void printGuestsForUserInputDate(List<Guest> guests) {
        System.out.println("Enter the date (YYYY-MM-DD) to search for guests:");
        String dateStr = scanner.nextLine();

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(dateStr, formatter);
            guestsForDay(guests, date);
        } catch (DateTimeParseException e) {
            logger.logError("Invalid date. Please enter a date in the format YYYY-MM-DD.");
        }
    }

    private void searchForMealType(Buffet buffet) {
        System.out.println("Enter the meal type to search:");
        String mealTypeStr = scanner.nextLine().toUpperCase();

        try {
            MealType mealType = MealType.valueOf(mealTypeStr);
            PriorityQueue<Meal> meals = buffet.searchMealType(mealType);
            if (!meals.isEmpty()) {
                logger.logInfo("Meals of type " + mealType + ":");
                for (Meal meal : meals) {
                    logger.logInfo(meal.getMealType() + " - " + meal.getMealType().getDurability() + " - " + meal.getTimestamp() + " cycle: " + meal.getCycle());
                }
            } else {
                logger.logInfo("No meals found for type " + mealType);
            }
        } catch (IllegalArgumentException e) {
            logger.logError("Invalid meal type. Please try again.");
        }
    }
}
