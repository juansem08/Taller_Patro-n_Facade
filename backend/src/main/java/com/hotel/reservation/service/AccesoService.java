package com.hotel.reservation.service;

import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class AccesoService {

    public String generarCodigoLlaveDigital() {
        return UUID.randomUUID().toString();
    }
}
