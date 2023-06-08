package com.codecool.ehotel.service.guest;

import com.codecool.ehotel.model.Guest;
import com.codecool.ehotel.model.GuestType;
import com.codecool.ehotel.model.MealType;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class GuestServiceImpl implements GuestService {

    private static final List<String> NAMES = List.of("John", "Alice", "Bob", "Jessica", "Mike", "Emma", "James", "Sophie", "Tom", "Lucy");
    private static final Random RANDOM = new Random();

    @Override
    public Guest generateRandomGuest(LocalDate seasonStart, LocalDate seasonEnd) {
        boolean isHappy = true;
        String name = NAMES.get(RANDOM.nextInt(NAMES.size()));
        GuestType guestType = GuestType.values()[RANDOM.nextInt(GuestType.values().length)];

        long seasonLength = ChronoUnit.DAYS.between(seasonStart, seasonEnd);
        long checkInDayOffset = RANDOM.nextInt((int) seasonLength);
        LocalDate checkInDate = seasonStart.plusDays(checkInDayOffset);

        int stayDuration = RANDOM.nextInt(7) + 1;
        LocalDate checkOutDate = checkInDate.plusDays(stayDuration);
        if (checkOutDate.isAfter(seasonEnd)) {
            checkOutDate = seasonEnd;
        }

        return new Guest(name, guestType, checkInDate, checkOutDate, stayDuration, isHappy);
    }

    @Override
    public Set<Guest> getGuestsForDay(List<Guest> guests, LocalDate date) {
        return guests.stream()
                .filter(guest -> !guest.checkIn().isAfter(date) && !guest.checkOut().isBefore(date))
                .collect(Collectors.toSet());
    }

    @Override
    public List<Set<Guest>> divideGuestsIntoGroups(List<Guest> guests, LocalDate date) {
        Set<Guest> guestsForDay = getGuestsForDay(guests, date);
        List<Guest> shuffledGuests = new ArrayList<>(guestsForDay);
        Collections.shuffle(shuffledGuests);

        List<Set<Guest>> guestGroups = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            guestGroups.add(new HashSet<>());
        }

        for (int i = 0; i < shuffledGuests.size(); i++) {
            Guest guest = shuffledGuests.get(i);
            int groupIndex = i % 8;
            guestGroups.get(groupIndex).add(guest);
        }

        return guestGroups;
    }

}
