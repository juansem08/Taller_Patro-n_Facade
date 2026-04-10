package com.hotel.reservation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Guest {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String idNumber;
}
