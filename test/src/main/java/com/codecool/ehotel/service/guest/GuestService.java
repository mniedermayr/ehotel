package com.codecool.ehotel.service.guest;

import com.codecool.ehotel.model.Guest;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface GuestService {

    Guest generateRandomGuest(LocalDate seasonStart, LocalDate seasonEnd);

    Set<Guest> getGuestsForDay(List<Guest> guests, LocalDate date);

    List<Set<Guest>> divideGuestsIntoGroups(List<Guest> guests, LocalDate date);
}
