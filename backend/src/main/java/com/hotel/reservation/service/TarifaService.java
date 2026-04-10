package com.hotel.reservation.service;

import com.hotel.reservation.model.RoomType;
import com.hotel.reservation.model.Season;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.EnumSet;

@Service
public class TarifaService {

    private final EnumSet<Month> highSeasonMonths = EnumSet.of(Month.DECEMBER, Month.JANUARY, Month.JULY, Month.AUGUST);

    public Season getSeasonForDate(LocalDate date) {
        return highSeasonMonths.contains(date.getMonth()) ? Season.HIGH : Season.LOW;
    }

    public double calcularPrecioTotal(RoomType type, LocalDate start, LocalDate end) {
        long nights = ChronoUnit.DAYS.between(start, end);
        if (nights <= 0) nights = 1; // Mínimo 1 noche

        // Tomamos la temporada basada en la fecha de inicio
        Season season = getSeasonForDate(start);
        
        return type.getBasePrice() * nights * season.getMultiplier();
    }

    public long calculateNights(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end);
    }
}
