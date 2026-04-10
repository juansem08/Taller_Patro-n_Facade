package com.hotel.reservation.service;

import com.hotel.reservation.model.Room;
import com.hotel.reservation.model.RoomType;
import com.hotel.reservation.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HabitacionService {
    private final HotelRepository hotelRepository;

    public List<Room> consultarDisponibilidad(LocalDate start, LocalDate end, RoomType type) {
        // En una implementacin real con DB, verificaramos colisiones de reservas.
        // Aqu, simplemente filtramos por tipo y disponibilidad actual.
        return hotelRepository.getAllRooms().stream()
                .filter(Room::isAvailable)
                .filter(room -> type == null || room.getType() == type)
                .collect(Collectors.toList());
    }

    public void reservarHabitacion(String roomId) {
        hotelRepository.updateRoomAvailability(roomId, false);
    }

    public void liberarHabitacion(String roomId) {
        hotelRepository.updateRoomAvailability(roomId, true);
    }
}
