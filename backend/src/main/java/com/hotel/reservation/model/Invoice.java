package com.hotel.reservation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
    private String id;
    private String reservationId;
    private String guestName;
    private double basePrice;
    private int numberOfNights;
    private double seasonMultiplier;
    private double additionalServicesTotal;
    private List<ServiceBreakdown> services;
    private double total;

    @Data
    @AllArgsConstructor
    public static class ServiceBreakdown {
        private String name;
        private double cost;
    }
}
