package com.mhdi.flightPrice.dto;

public record FlightResponse(String origin, String destination, String date, String flightNumber, double price) {}
