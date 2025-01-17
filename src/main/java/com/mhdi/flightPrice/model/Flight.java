package com.mhdi.flightPrice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "tbl_flight",
    uniqueConstraints = @UniqueConstraint(columnNames = {"flightNumber", "date"})
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String origin;
    private String destination;
    @NotNull
    private String date;
    @NotNull
    private String flightNumber;
    private double price;

    public Flight(String origin, String destination, String date, String flightNumber, double price) {
        this.origin = origin;
        this.destination = destination;
        this.date = date;
        this.flightNumber = flightNumber;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", date='" + date + '\'' +
                ", flightNumber='" + flightNumber + '\'' +
                ", price=" + price +
                '}';
    }
}
