package com.mhdi.flightPrice.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mhdi.flightPrice.dto.FlightRequest;
import com.mhdi.flightPrice.dto.FlightResponse;
import com.mhdi.flightPrice.model.Flight;
import com.mhdi.flightPrice.service.FlightService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
@Slf4j
class FlightController {

    private final FlightService flightService;

    @PostMapping("/add")
    public ResponseEntity<String> addFlight(@Valid @RequestBody FlightRequest flightRequest) {
        try {
            flightService.addFlight(flightRequest);
            log.info("Flight added successfully: {}", flightRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Flight added successfully!");
        } catch (IllegalArgumentException e) {
            log.warn("Failed to add flight: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<FlightResponse>> searchFlights(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam String date) {

        List<Flight> flights = flightService.searchFlights(origin, destination, date);
          List<FlightResponse> flightResponses= flights.stream()
            .map(this::toFlightResponse)
            .collect(Collectors.toList());        
            log.info("Search completed for {} -> {} on {}. Found {} flights.", origin, destination, date, flights.size());
            return ResponseEntity.ok(flightResponses);
    }



    private FlightResponse toFlightResponse(Flight flight) {
        return new FlightResponse(
            flight.getOrigin(),
            flight.getDestination(),
            flight.getDate(),
            flight.getFlightNumber(),
            flight.getPrice()
        );
    }
    
}
