package com.hotel.reservation.model;

public enum RoomType {
    SINGLE(100.0),
    DOUBLE(150.0),
    SUITE(250.0);

    private final double basePrice;

    RoomType(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getBasePrice() {
        return basePrice;
    }
}
