package com.hotel.reservation.model;

public enum ServiceType {
    SPA(50.0),
    BREAKFAST(15.0),
    TRANSPORT(30.0);

    private final double cost;

    ServiceType(double cost) {
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }
}
