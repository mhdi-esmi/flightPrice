package com.mhdi.flightPrice.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.mhdi.flightPrice.model.Flight;

@DataJpaTest
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)

class FlightRepositoryTest {

    @Autowired
    private FlightRepository flightRepository;



    @Test
    void findByOriginAndDestinationAndDate_ShouldReturnMatchingFlights() {
        String origin = "TEH";
        String destination = "LON";
        String date = "2025-01-11";

        List<Flight> flights = flightRepository.findByOriginAndDestinationAndDate(origin, destination, date);

        assertNotNull(flights);
        assertFalse(flights.isEmpty());
    }

    @Test
    void findByOriginAndDestinationAndDateLike_ShouldReturnFlightsMatchingPattern() {
        String origin = "TEH";
        String destination = "LOn";
        String date = "2025-01-11";

        List<Flight> flights = flightRepository.findByOriginAndDestinationAndDateLike(origin, destination, date);

        assertNotNull(flights);
        assertFalse(flights.isEmpty());
        assertTrue(flights.stream().allMatch(f -> f.getOrigin().contains("TEH") && f.getDestination().contains("LO")));
    }

    @Test
    void existsByFlightNumberAndDate_ShouldReturnTrueIfExists() {
        String flightNumber = "FL123";
        String date = "2025-01-11";

        boolean exists = flightRepository.existsByFlightNumberAndDate(flightNumber, date);

        assertTrue(exists);
    }

    @Test
    void existsByFlightNumberAndDate_ShouldReturnFalseIfNotExists() {

        String flightNumber = "NON_EXISTENT";
        String date = "2025-01-11";

        boolean exists = flightRepository.existsByFlightNumberAndDate(flightNumber, date);

        assertFalse(exists);
    }
}
