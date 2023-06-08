package com.codecool.ehotel.model;

import java.time.LocalDate;
public record Guest(String name, GuestType guestType, LocalDate checkIn, LocalDate checkOut, int stayDuration, boolean isHappy) {
    public Guest {
        if (name == null || guestType == null || checkIn == null || checkOut == null) {
            throw new NullPointerException();
        }

        if (stayDuration < 1) {
            throw new IllegalArgumentException("Stay duration must be at least 1 day.");
        }
    }
}




