package com.mhdi.flightPrice.service;

import com.mhdi.flightPrice.dto.FlightRequest;
import com.mhdi.flightPrice.model.Flight;
import com.mhdi.flightPrice.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private PartnerService partnerService;

    @InjectMocks
    private FlightService flightService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); 
    }

    @Test
    void testAddFlight() {
        FlightRequest flightRequest = new FlightRequest("TEH", "SHI", "2025-01-11", "FL123", 200.0);
            
        flightService.addFlight(flightRequest);

        verify(flightRepository).save(any(Flight.class));
    }

    @Test
    void testSearchFlights() {

        List<Flight> internalFlights = List.of(
                new Flight("TEH", "SHI", "2025-01-11", "FL123", 300.0)
        );
        List<Flight> partnerFlights = List.of(
                new Flight("TEH", "SHI", "2025-01-11", "PA123", 200.0)
        );

        when(flightRepository.findByOriginAndDestinationAndDateLike("TEH", "SHI", "2025-01-11")).thenReturn(internalFlights);
        when(partnerService.getPartnerFlightsAsync("TEH", "SHI", "2025-01-11")).thenReturn(partnerFlights);

        List<Flight> result = flightService.searchFlights("TEH", "SHI", "2025-01-11");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("PA123", result.get(0).getFlightNumber()); // Sorted by price
        assertEquals("FL123", result.get(1).getFlightNumber());

        verify(flightRepository).findByOriginAndDestinationAndDateLike("TEH", "SHI", "2025-01-11");
        verify(partnerService).getPartnerFlightsAsync("TEH", "SHI", "2025-01-11");
    }

    @Test
    void testSearchFlights_NoFlightsFound() {

        when(flightRepository.findByOriginAndDestinationAndDateLike("TEH", "SHI", "2025-01-11")).thenReturn(List.of());
        when(partnerService.getPartnerFlightsAsync("TEH", "SHI", "2025-01-11")).thenReturn(List.of());

        List<Flight> result = flightService.searchFlights("TEH", "SHI", "2025-01-11");

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(flightRepository).findByOriginAndDestinationAndDateLike("TEH", "SHI", "2025-01-11");
        verify(partnerService).getPartnerFlightsAsync("TEH", "SHI", "2025-01-11");
    }

    @Test
    void addFlight_ShouldThrowException_WhenDuplicateFlightExists() {
        FlightRequest flightRequest = new FlightRequest("Origin", "Destination", "2025-01-15", "F123", 200.0);
        Mockito.when(flightRepository.existsByFlightNumberAndDate("F123", "2025-01-15")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> flightService.addFlight(flightRequest));
    }
}
