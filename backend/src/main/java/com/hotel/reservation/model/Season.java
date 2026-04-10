package com.hotel.reservation.model;

public enum Season {
    HIGH(1.5),
    LOW(1.0);

    private final double multiplier;

    Season(double multiplier) {
        this.multiplier = multiplier;
    }

    public double getMultiplier() {
        return multiplier;
    }
}
