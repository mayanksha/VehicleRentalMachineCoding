package com.navi.strategy.impl;

import com.navi.exception.EntityNotFoundException;
import com.navi.model.Vehicle;
import com.navi.service.VehicleService;
import com.navi.strategy.VehicleSelectionStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class LowestPriceVehicleSelectionStrategy implements VehicleSelectionStrategy {
    private VehicleService vehicleService = VehicleService.getInstance();
    final Double INVALID_PRICE = -1.0;

    @Override
    public Map.Entry<String, Double> pickVehicleAndGetBookingPrice(List<String> vehicleIDs) {
        Map.Entry<String, Double> result = new AbstractMap.SimpleEntry<>(null, INVALID_PRICE);

        for (String vehicleID : vehicleIDs) {
            try {
                Vehicle vehicle = vehicleService.getVehicleForID(vehicleID);

                if (result.getValue() == INVALID_PRICE) {
                    result = new AbstractMap.SimpleEntry<>(vehicle.getID(), vehicle.getPrice());
                } else {
                    if (result.getValue() > vehicle.getPrice()) {
                        result = new AbstractMap.SimpleEntry<>(vehicle.getID(), vehicle.getPrice());
                    }
                }
            } catch (EntityNotFoundException e) {
                continue;
            }
        }

        return result;
    }

    private static LowestPriceVehicleSelectionStrategy instance;

    public static LowestPriceVehicleSelectionStrategy getInstance() {
        if (instance == null)
            instance = new LowestPriceVehicleSelectionStrategy();
        return instance;
    }

    private LowestPriceVehicleSelectionStrategy() {

    }

}
