package com.navi.strategy;

import java.util.List;
import java.util.Map;

public interface VehicleSelectionStrategy {
    /**
     * @param vehicleIDs
     * @return Returns a pair containing the
     */
    Map.Entry<String, Double> pickVehicleAndGetBookingPrice(List<String> vehicleIDs);
}
