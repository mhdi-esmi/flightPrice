package com.mhdi.flightPrice.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.mhdi.flightPrice.model.Flight;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class PartnerService {


    private final double minPrice=100;

    public List<Flight> getPartnerFlightsAsync(String origin, String destination, String date) {
        CompletableFuture<List<Flight>> partnerA = CompletableFuture.supplyAsync(() -> callPartnerA(origin, destination, date))
                                                                    .exceptionally(ex-> new ArrayList<Flight>());
        CompletableFuture<List<Flight>> partnerB = CompletableFuture.supplyAsync(() -> callPartnerB(origin, destination, date))
                                                                    .exceptionally(ex-> new ArrayList<Flight>());;
        CompletableFuture<List<Flight>> partnerC = CompletableFuture.supplyAsync(() -> callPartnerC(origin, destination, date))
                                                                    .exceptionally(ex-> new ArrayList<Flight>());

        List<Flight> flights = CompletableFuture.allOf(partnerA, partnerB, partnerC)
                .thenApply(v -> Stream.of(partnerA, partnerB, partnerC)
                        .map(CompletableFuture::join)
                        .flatMap(List::stream)
                        .collect(Collectors.toList()))
                .join();
        log.info("Fetched {} flights from partners.", flights.size());

        return flights;
    }

    private List<Flight> callPartnerA(String origin, String destination, String date) {
     
        return List.of(
                new Flight(origin, destination, date, "PA123", getRandomPrice()),
                new Flight(origin, destination, date, "PA124", getRandomPrice())
        );
    }

    private List<Flight> callPartnerB(String origin, String destination, String date) {

        return List.of(
                new Flight(origin, destination, date, "PB123", getRandomPrice()),
                new Flight(origin, destination, date, "PB124", getRandomPrice())
        );
    }

    private List<Flight> callPartnerC(String origin, String destination, String date) {
    
        return List.of(
                new Flight(origin, destination, date, "PC123", getRandomPrice()),
                new Flight(origin, destination, date, "PC124", getRandomPrice())
        );
    }

    private double getRandomPrice(){
        var random=new Random();
        return (double) minPrice+random.nextInt(100);
       
    }
}