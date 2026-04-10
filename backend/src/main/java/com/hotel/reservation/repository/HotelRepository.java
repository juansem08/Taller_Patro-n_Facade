package com.hotel.reservation.repository;

import com.hotel.reservation.model.Room;
import com.hotel.reservation.model.RoomType;
import com.hotel.reservation.model.Reservation;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class HotelRepository {
    private final Map<String, Room> rooms = new ConcurrentHashMap<>();
    private final Map<String, Reservation> reservations = new ConcurrentHashMap<>();

    public HotelRepository() {
        // Initialize with 15+ rooms
        for (int i = 101; i <= 105; i++) {
            rooms.put(String.valueOf(i), new Room(String.valueOf(i), i, RoomType.SINGLE, true, "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?auto=format&fit=crop&q=80&w=1000"));
        }
        for (int i = 201; i <= 205; i++) {
            rooms.put(String.valueOf(i), new Room(String.valueOf(i), i, RoomType.DOUBLE, true, "https://images.unsplash.com/photo-1590490360182-c33d57733427?auto=format&fit=crop&q=80&w=1000"));
        }
        for (int i = 301; i <= 305; i++) {
            rooms.put(String.valueOf(i), new Room(String.valueOf(i), i, RoomType.SUITE, true, "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?auto=format&fit=crop&q=80&w=1000"));
        }
    }

    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }

    public Optional<Room> getRoomById(String id) {
        return Optional.ofNullable(rooms.get(id));
    }

    public void saveReservation(Reservation reservation) {
        reservations.put(reservation.getId(), reservation);
    }

    public Optional<Reservation> getReservationById(String id) {
        return Optional.ofNullable(reservations.get(id));
    }

    public void updateRoomAvailability(String roomId, boolean available) {
        Room room = rooms.get(roomId);
        if (room != null) {
            room.setAvailable(available);
        }
    }
}
