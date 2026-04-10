package com.hotel.reservation.controller;

import com.hotel.reservation.facade.HotelFacade;
import com.hotel.reservation.model.*;
import com.hotel.reservation.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/hotel")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Para facilitar el desarrollo con React
public class HotelController {

    private final HotelFacade hotelFacade;
    private final HotelRepository hotelRepository;

    @GetMapping("/disponibilidad")
    public List<Room> getDisponibilidad(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) RoomType type) {
        
        // Simplemente devolvemos todas las habitaciones disponibles por ahora
        return hotelRepository.getAllRooms().stream()
                .filter(Room::isAvailable)
                .filter(room -> type == null || room.getType() == type)
                .toList();
    }

    @PostMapping("/reservar")
    public ResponseEntity<Reservation> crearReserva(@RequestBody ReservationDTO reservaDTO) {
        return ResponseEntity.ok(hotelFacade.crearReserva(reservaDTO));
    }

    @PostMapping("/servicios/{reservaId}")
    public ResponseEntity<String> agregarServicio(
            @PathVariable String reservaId,
            @RequestParam ServiceType tipoServicio) {
        hotelFacade.agregarServicio(reservaId, tipoServicio);
        return ResponseEntity.ok("Servicio agregado exitosamente");
    }

    @PutMapping("/checkin/{reservaId}")
    public ResponseEntity<Reservation> realizarCheckIn(@PathVariable String reservaId) {
        return ResponseEntity.ok(hotelFacade.realizarCheckIn(reservaId));
    }

    @PutMapping("/checkout/{reservaId}")
    public ResponseEntity<Invoice> realizarCheckOut(@PathVariable String reservaId) {
        return ResponseEntity.ok(hotelFacade.realizarCheckOut(reservaId));
    }

    @GetMapping("/reserva/{reservaId}")
    public ResponseEntity<Reservation> getReserva(@PathVariable String reservaId) {
        return ResponseEntity.ok(hotelFacade.getReserva(reservaId));
    }
}
