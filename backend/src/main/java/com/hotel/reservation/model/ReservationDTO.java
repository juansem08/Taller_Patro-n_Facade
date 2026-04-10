package com.hotel.reservation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {
    private String roomId;
    private Guest guest;
    private LocalDate startDate;
    private LocalDate endDate;
}
