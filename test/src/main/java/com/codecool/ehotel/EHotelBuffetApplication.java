package com.codecool.ehotel;

import com.codecool.ehotel.service.BreakfastManager;
import com.codecool.ehotel.service.buffet.BuffetService;
import com.codecool.ehotel.service.buffet.BuffetServiceImpl;
import com.codecool.ehotel.service.guest.GuestService;
import com.codecool.ehotel.service.guest.GuestServiceImpl;
import com.codecool.ehotel.service.logger.ConsoleLogger;
import com.codecool.ehotel.service.logger.Logger;

import java.util.*;

public class EHotelBuffetApplication {
    public static void main(String[] args) {
        Logger logger = new ConsoleLogger();
        BuffetService buffetService = new BuffetServiceImpl();
        GuestService guestService = new GuestServiceImpl();
        Scanner scanner = new Scanner(System.in);
        BreakfastManager breakfastService = new BreakfastManager(logger, buffetService);

        ApplicationInterface applicationInterface = new ApplicationInterface(logger, buffetService, guestService, scanner, breakfastService);
        applicationInterface.run();
    }
}

