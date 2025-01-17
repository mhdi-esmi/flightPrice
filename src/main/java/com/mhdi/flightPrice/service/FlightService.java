package com.mhdi.flightPrice.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.mhdi.flightPrice.dto.FlightRequest;
import com.mhdi.flightPrice.model.Flight;
import com.mhdi.flightPrice.repository.FlightRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightService {


    
    private final FlightRepository flightRepository;
    private final PartnerService partnerService;




    public void addFlight(FlightRequest flightRequest) {
        
        if (isDuplicateFlight(flightRequest.flightNumber(), flightRequest.date())) {
            throw new IllegalArgumentException("A flight with the same flight number already exists for the given date.");
        }
        Flight flight = new Flight(
            flightRequest.origin(),
            flightRequest.destination(),
            flightRequest.date(),
            flightRequest.flightNumber(),
            flightRequest.price()
    );
        flightRepository.save(flight);
        log.info("Flight saved successfully.");

    }


    @Cacheable(value = "flights", key = "#origin + #destination + #date", unless = "#result == null || #result.isEmpty()")
    public List<Flight> searchFlights(String origin, String destination, String date) {

        List<Flight> internalFlights = flightRepository.findByOriginAndDestinationAndDateLike(origin, destination, date);

        List<Flight> partnerFlights = partnerService.getPartnerFlightsAsync(origin, destination, date);


        
        log.info("Total flights found: {}", internalFlights.size()+partnerFlights.size());

        return Stream.concat(internalFlights.stream(), partnerFlights.stream())
                .sorted(Comparator.comparingDouble(Flight::getPrice))
                .collect(Collectors.toList());

    }

    private boolean isDuplicateFlight(String flightNumber, String date) {
        return flightRepository.existsByFlightNumberAndDate(flightNumber, date);
    }
}
