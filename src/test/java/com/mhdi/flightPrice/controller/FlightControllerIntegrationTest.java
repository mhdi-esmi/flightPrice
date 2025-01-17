package com.mhdi.flightPrice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mhdi.flightPrice.dto.FlightRequest;
import com.mhdi.flightPrice.dto.FlightResponse;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FlightControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void addAndSearchFlights_ShouldWorkCorrectly() {

        FlightRequest newFlight = new FlightRequest("Origin", "Destination", "2025-01-15", "F123", 1.0);
        ResponseEntity<String> addResponse = restTemplate.postForEntity("/api/flights/add", newFlight, String.class);

        assertEquals(HttpStatus.CREATED, addResponse.getStatusCode());
        assertTrue(addResponse.getBody().contains("Flight added successfully"));

       ResponseEntity<FlightResponse[]> searchResponse = restTemplate.getForEntity(
                "/api/flights/search?origin=Origin&destination=Destination&date=2025-01-15", FlightResponse[].class);


        assertEquals(HttpStatus.OK, searchResponse.getStatusCode()); 
        assertNotNull(searchResponse.getBody());
        assertTrue(searchResponse.getBody().length >= 1);
        assertEquals(newFlight.flightNumber(), searchResponse.getBody()[0].flightNumber());
    }
}
