package com.navi.service;

import com.navi.exception.DuplicateEntityException;
import com.navi.exception.EntityNotFoundException;
import com.navi.model.Vehicle;
import com.navi.repository.VehicleRepository;

public class VehicleService {

    private final VehicleRepository vehicleRepository = VehicleRepository.getInstance();

    public Vehicle createAndInsertNewVehicle(final String vehicleType, final String vehicleID, final Double price) throws DuplicateEntityException {
        Vehicle vehicle = new Vehicle(vehicleID, vehicleType, price);

        vehicleRepository.insertVehicleForId(vehicle.getID(), vehicle);
        return vehicle;
    }

    public Vehicle getVehicleForID(final String ID) throws EntityNotFoundException {
        return vehicleRepository.getVehicleForId(ID);
    }

    private static VehicleService instance;
    public static VehicleService getInstance() {
        if (instance == null)
            instance = new VehicleService();
        return instance;
    }

    private VehicleService() {

    }

}
