package com.navi.model;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
public class Vehicle implements Comparable<Vehicle> {

    /**
     * Vehicle registration ID
     */
    @NonNull
    private String ID;

    /**
     * Type of the vehicle
     */
    @NonNull
    private String type;

    /**
     * Vehicle price
     */
    private Double price;


    public Vehicle(@NonNull String ID, @NonNull String type, Double price) {
        this.ID = ID;
        this.type = type;
        this.price = price;
    }

    @Override
    public int compareTo(Vehicle o) {
        return price.compareTo(o.getPrice());
    }
}
