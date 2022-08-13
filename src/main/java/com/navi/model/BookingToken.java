package com.navi.model;

import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

@Getter
public class BookingToken {

    /**
     * Unique identifier for this booking
     */
    @NonNull
    private String ID;

    /**
     * Price for the booking (for the whole duration)
     */
    @NonNull
    private Double price;

    /**
     * The time when the booking happened
     */
    @NonNull
    private Integer startTime;

    /**
     * The time until when the booking lasts
     */
    @NonNull
    private Integer endTime;

    /**
     * The ID of the vehicle which got booked
     */
    @NonNull
    private String vehicleID;

    /**
     * The branch from where the vehicle was booked
     */
    @NonNull
    private String branchID;

    public BookingToken(@NonNull String branchID, @NonNull String vehicleID, @NonNull Double price, @NonNull Integer startTime, @NonNull Integer endTime) {
        this.ID = UUID.randomUUID().toString();
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.vehicleID = vehicleID;
        this.branchID = branchID;
    }
}
