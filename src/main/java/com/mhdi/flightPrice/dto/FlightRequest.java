package com.mhdi.flightPrice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record FlightRequest( 
@NotBlank String origin,
@NotBlank String destination,
@NotBlank String date,
@NotBlank String flightNumber,
@Positive double price) {}

