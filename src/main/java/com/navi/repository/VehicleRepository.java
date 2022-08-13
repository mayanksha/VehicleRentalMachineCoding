package com.navi.repository;

import com.navi.exception.DuplicateEntityException;
import com.navi.exception.EntityNotFoundException;
import com.navi.model.Vehicle;

import java.util.HashMap;
import java.util.Map;

public class VehicleRepository {
    private final Map<String, Vehicle> db = new HashMap<>();

    public Vehicle getVehicleForId(String ID) throws EntityNotFoundException {
        if (!db.containsKey(ID)) {
            throw new EntityNotFoundException(Vehicle.class.getName(), ID);
        }

        return db.get(ID);
    }

    public void insertVehicleForId(String ID, Vehicle vehicle) throws DuplicateEntityException {
        if (db.containsKey(ID)) {
            throw new DuplicateEntityException(Vehicle.class.getName(), ID);
        }

        db.put(ID, vehicle);
    }

    private static VehicleRepository instance;

    public static VehicleRepository getInstance() {
        if (instance == null)
            instance = new VehicleRepository();
        return instance;
    }

    private VehicleRepository() {

    }

}
