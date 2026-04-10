package com.hotel.reservation.facade;

import com.hotel.reservation.model.Invoice;
import com.hotel.reservation.model.Reservation;
import com.hotel.reservation.model.ReservationDTO;
import com.hotel.reservation.model.ServiceType;

public interface HotelFacade {
    Reservation crearReserva(ReservationDTO reservaDTO);
    void agregarServicio(String reservaId, ServiceType tipoServicio);
    Reservation realizarCheckIn(String reservaId);
    Invoice realizarCheckOut(String reservaId);
    Reservation getReserva(String reservaId);
}
