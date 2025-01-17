package com.mhdi.flightPrice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mhdi.flightPrice.model.Flight;


@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    
    List<Flight> findByOriginAndDestinationAndDate(String origin, String destination, String date);

    @Query("SELECT f FROM Flight f WHERE LOWER(f.origin) LIKE LOWER(CONCAT('%', :origin, '%')) AND LOWER(f.destination) LIKE LOWER(CONCAT('%', :destination, '%')) AND f.date = :date") 
    List<Flight> findByOriginAndDestinationAndDateLike(@Param("origin") String origin, @Param("destination") String destination,String date);

    boolean existsByFlightNumberAndDate(String flightNumber, String date);

}
