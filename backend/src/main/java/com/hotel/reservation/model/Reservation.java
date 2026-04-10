package com.hotel.reservation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    private String id;
    private Room room;
    private Guest guest;
    private LocalDate startDate;
    private LocalDate endDate;
    private ReservationStatus status;
    private List<ServiceType> additionalServices = new ArrayList<>();
    private String digitalKey;

    public enum ReservationStatus {
        PENDING, CHECKED_IN, CHECKED_OUT
    }
}
