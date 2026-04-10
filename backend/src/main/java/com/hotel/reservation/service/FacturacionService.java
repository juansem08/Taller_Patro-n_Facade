package com.hotel.reservation.service;

import com.hotel.reservation.model.Invoice;
import com.hotel.reservation.model.Reservation;
import com.hotel.reservation.model.Season;
import com.hotel.reservation.model.ServiceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacturacionService {

    private final TarifaService tarifaService;

    public Invoice generarFactura(Reservation reservation) {
        long nights = ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());
        if (nights <= 0) nights = 1;

        Season season = tarifaService.getSeasonForDate(reservation.getStartDate());
        double baseRate = reservation.getRoom().getType().getBasePrice();
        double basePriceTotal = baseRate * nights * season.getMultiplier();

        List<Invoice.ServiceBreakdown> serviceBreakdowns = new ArrayList<>();
        double additionalTotal = 0.0;

        if (reservation.getAdditionalServices() != null) {
            for (ServiceType type : reservation.getAdditionalServices()) {
                serviceBreakdowns.add(new Invoice.ServiceBreakdown(type.name(), type.getCost()));
                additionalTotal += type.getCost();
            }
        }

        double finalTotal = basePriceTotal + additionalTotal;

        return Invoice.builder()
                .id("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .reservationId(reservation.getId())
                .guestName(reservation.getGuest().getFirstName() + " " + reservation.getGuest().getLastName())
                .basePrice(basePriceTotal)
                .numberOfNights((int) nights)
                .seasonMultiplier(season.getMultiplier())
                .additionalServicesTotal(additionalTotal)
                .services(serviceBreakdowns)
                .total(finalTotal)
                .build();
    }
}
