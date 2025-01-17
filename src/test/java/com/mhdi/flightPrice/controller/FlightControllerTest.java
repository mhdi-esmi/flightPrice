package com.mhdi.flightPrice.controller;

import com.mhdi.flightPrice.dto.FlightRequest;
import com.mhdi.flightPrice.dto.FlightResponse;
import com.mhdi.flightPrice.model.Flight;
import com.mhdi.flightPrice.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@WebMvcTest(FlightController.class)
class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightService flightService;

    private Flight flight;

    @BeforeEach
    void setUp() {
        flight = new Flight("TEH", "LON", "2025-01-11", "FL123", 200.0);
    }

    @Test
    void testAddFlight() throws Exception {
        mockMvc.perform(post("/api/flights/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "origin": "TEH",
                                    "destination": "LON",
                                    "date": "2025-01-11",
                                    "flightNumber": "FL123",
                                    "price": 200.0
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().string("Flight added successfully!"));

        verify(flightService).addFlight(any(FlightRequest.class));
    }

    @Test
    void testSearchFlights() throws Exception {
        List<Flight> mockFlights = List.of(flight);
          List<FlightResponse> mockFlightResponses = mockFlights.stream()
                .map(f -> new FlightResponse(f.getOrigin(), f.getDestination(), f.getDate(), f.getFlightNumber(), f.getPrice()))
                .toList();
        when(flightService.searchFlights(eq("TEH"), eq("LON"), eq("2025-01-11"))).thenReturn(mockFlights);

        mockMvc.perform(get("/api/flights/search")
                        .param("origin", "TEH")
                        .param("destination", "LON")
                        .param("date", "2025-01-11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(mockFlightResponses.size()))
                .andExpect(jsonPath("[0].origin", is("TEH")))
                .andExpect(jsonPath("[0].destination", is("LON")))
                .andExpect(jsonPath("[0].date", is("2025-01-11")))
                .andExpect(jsonPath("[0].flightNumber", is("FL123")))
                .andExpect(jsonPath("[0].price", is(200.0)));

        verify(flightService).searchFlights("TEH", "LON", "2025-01-11");
    }

    @Test
    void testAddFlightWithInvalidRequest() throws Exception {
        mockMvc.perform(post("/api/flights/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "origin": "",
                                    "destination": "ESF",
                                    "date": "",
                                    "flightNumber": "FL123",
                                    "price": -200.0
                                }
                                """))                               
                .andExpect(status().isBadRequest());      

        Mockito.verifyNoInteractions(flightService);
    }
}
