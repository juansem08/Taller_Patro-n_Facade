package com.hotel.reservation.facade;

import com.hotel.reservation.model.*;
import com.hotel.reservation.repository.HotelRepository;
import com.hotel.reservation.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class HotelFacadeImpl implements HotelFacade {

    private final HabitacionService habitacionService;
    private final TarifaService tarifaService;
    private final ServicioAdicionalService servicioAdicionalService;
    private final FacturacionService facturacionService;
    private final AccesoService accesoService;
    private final HotelRepository hotelRepository;

    @Override
    public Reservation crearReserva(ReservationDTO reservaDTO) {
        Room room = hotelRepository.getRoomById(reservaDTO.getRoomId())
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));
        
        if (!room.isAvailable()) {
            throw new RuntimeException("Habitación no disponible");
        }

        Reservation reservation = Reservation.builder()
                .id(UUID.randomUUID().toString().substring(0, 8))
                .room(room)
                .guest(reservaDTO.getGuest())
                .startDate(reservaDTO.getStartDate())
                .endDate(reservaDTO.getEndDate())
                .status(Reservation.ReservationStatus.PENDING)
                .build();

        habitacionService.reservarHabitacion(room.getId());
        hotelRepository.saveReservation(reservation);
        
        return reservation;
    }

    @Override
    public void agregarServicio(String reservaId, ServiceType tipoServicio) {
        Reservation reservation = hotelRepository.getReservationById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        
        servicioAdicionalService.agregarServicio(reservation, tipoServicio);
    }

    @Override
    public Reservation realizarCheckIn(String reservaId) {
        Reservation reservation = hotelRepository.getReservationById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        
        reservation.setStatus(Reservation.ReservationStatus.CHECKED_IN);
        reservation.setDigitalKey(accesoService.generarCodigoLlaveDigital());
        
        return reservation;
    }

    @Override
    public Invoice realizarCheckOut(String reservaId) {
        Reservation reservation = hotelRepository.getReservationById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        
        Invoice invoice = facturacionService.generarFactura(reservation);
        
        reservation.setStatus(Reservation.ReservationStatus.CHECKED_OUT);
        habitacionService.liberarHabitacion(reservation.getRoom().getId());
        
        return invoice;
    }

    @Override
    public Reservation getReserva(String reservaId) {
        return hotelRepository.getReservationById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
    }
}
