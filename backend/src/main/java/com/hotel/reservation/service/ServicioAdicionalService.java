package com.hotel.reservation.service;

import com.hotel.reservation.model.Reservation;
import com.hotel.reservation.model.ServiceType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ServicioAdicionalService {

    public void agregarServicio(Reservation reservation, ServiceType type) {
        if (reservation.getAdditionalServices() == null) {
            reservation.setAdditionalServices(new ArrayList<>());
        }
        reservation.getAdditionalServices().add(type);
    }
}
