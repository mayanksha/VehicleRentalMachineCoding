package com.navi.repository;

import com.navi.exception.DuplicateEntityException;
import com.navi.exception.EntityNotFoundException;
import com.navi.model.Branch;

import java.util.*;

public class BranchVehicleRepository {
    /**
     * Stores the branchID -> {vehicleType -> {vehicleIDs}} mapping
     */
    private final Map<String, Map<String, Set<String>>> db = new HashMap<>();

    public List<String> getAllVehicleIDsForAType(final String branchID, final String vehicleType) throws EntityNotFoundException {
        if (!db.containsKey(branchID)) {
            throw new EntityNotFoundException("branchID (in branch-vehicle mapping)", branchID);
        }

        if (!db.get(branchID).containsKey(vehicleType)) {
            throw new EntityNotFoundException("vehicleType (in branch-vehicle mapping)", vehicleType);
        }

        return new ArrayList<>(db.get(branchID).get(vehicleType));
    }

    public void insertVehicleToBranch(final String branchID, final String vehicleType, final String vehicleID) throws DuplicateEntityException {
        if (!db.containsKey(branchID)) {
            Set<String> vehicleSet = new HashSet<>();
            vehicleSet.add(vehicleID);

            Map<String, Set<String>> typeMap = new HashMap<>();
            typeMap.put(vehicleType, vehicleSet);

            db.put(branchID, typeMap);
            return;
        }

        if (!db.get(branchID).containsKey(vehicleType)) {
            Set<String> vehicleSet = new HashSet<>();
            vehicleSet.add(vehicleID);

            db.get(branchID).put(vehicleType, vehicleSet);
            return;
        }

        if (db.get(branchID).get(vehicleType).contains(vehicleID)) {
            throw new DuplicateEntityException("vehicleType (in branch-vehicle mapping)", vehicleID);
        }

        db.get(branchID).get(vehicleType).add(vehicleID);
    }

    private static BranchVehicleRepository instance;

    public static BranchVehicleRepository getInstance() {
        if (instance == null)
            instance = new BranchVehicleRepository();
        return instance;
    }

    private BranchVehicleRepository() {

    }

}
