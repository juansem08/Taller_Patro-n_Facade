package com.hotel.reservation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    private String id;
    private int number;
    private RoomType type;
    private boolean available;
    private String imageUrl; // For the "WOW" frontend
}
