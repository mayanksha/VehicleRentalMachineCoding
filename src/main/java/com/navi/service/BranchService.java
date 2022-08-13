package com.navi.service;

import com.navi.exception.DuplicateEntityException;
import com.navi.exception.EntityNotFoundException;
import com.navi.exception.RentalServiceException;
import com.navi.model.BookingToken;
import com.navi.model.Branch;
import com.navi.model.Vehicle;
import com.navi.repository.BookingRepository;
import com.navi.repository.BranchRepository;
import com.navi.repository.BranchVehicleRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class BranchService {

    private final BranchRepository branchRepository = BranchRepository.getInstance();
    private final BookingRepository bookingRepository = BookingRepository.getInstance();
    private final BranchVehicleRepository branchVehicleRepository = BranchVehicleRepository.getInstance();

    private final Map<String, Set<String>> branchVehicleTypes = new HashMap<>();

    public void createAndInsertNewBranch(final String name, final String address) throws DuplicateEntityException {
        Branch branch = new Branch(name, address);

        branchRepository.insertBranchForId(branch.getID(), branch);
    }

    public boolean checkValidVehicleTypeForBranch(final String branchID, final String vehicleType) throws RentalServiceException {
        if (branchVehicleTypes.get(branchID) == null) {
            return false;
        }

        return branchVehicleTypes.get(branchID).contains(vehicleType);
    }

    public void addVehicleTypesToBranch(final String branchID, List<String> vehicleTypes) throws RentalServiceException {
        if (vehicleTypes.isEmpty()) {
            throw new RentalServiceException("Empty vehicleTypes list is not allowed");
        }

        if (!branchVehicleTypes.containsKey(branchID)) {
            branchVehicleTypes.put(branchID, new HashSet<>(vehicleTypes));
        } else {
            branchVehicleTypes.get(branchID).addAll(vehicleTypes);
        }
    }

    public void addVehicleToBranch(final String branchID, final Vehicle vehicle) throws DuplicateEntityException {
        branchVehicleRepository.insertVehicleToBranch(branchID, vehicle.getType(), vehicle.getID());
    }

    public List<String> getAllVehiclesOfSpecificType(final String branchID, final String vehicleType) throws EntityNotFoundException {
        return branchVehicleRepository.getAllVehicleIDsForAType(branchID, vehicleType);
    }

    public List<String> getAllUnbookedVehiclesOfTypeInRange(final String branchID, final String vehicleType, Integer start, Integer end) throws EntityNotFoundException {
        assert (start < end);

        List<String> allVehicleIDsForAType = branchVehicleRepository.getAllVehicleIDsForAType(branchID, vehicleType);
        List<String> result = new ArrayList<>();

        updateResultWithAvailableVehicle(start, end, result, allVehicleIDsForAType);

        return result;
    }

    public List<String> getAllUnbookedVehiclesInRange(final String branchID, Integer start, Integer end) {
        assert (start < end);
        List<String> result = new ArrayList<>();

        for (String vehicleType : branchVehicleTypes.get(branchID)) {
            List<String> allVehicleIDsForAType = null;
            try {
                allVehicleIDsForAType = branchVehicleRepository.getAllVehicleIDsForAType(branchID, vehicleType);
            } catch (EntityNotFoundException e) {
                log.debug("No vehicle was registered for vehicleType: {}", vehicleType);
                continue;
            }

            updateResultWithAvailableVehicle(start, end, result, allVehicleIDsForAType);
        }

        return result;
    }

    private void updateResultWithAvailableVehicle(Integer start, Integer end, List<String> result, List<String> allVehicleIDsForAType) {
        for (String vehicleID : allVehicleIDsForAType) {
            List<BookingToken> tokens = bookingRepository.getAllTokensForVehicleID(vehicleID);

            // we check if there's any token which falls in the given time range. If so, we can't choose this vehicle
            boolean isVehicleFree = true;
            for (BookingToken token : tokens) {
                if ((start >= token.getStartTime() && start < token.getEndTime()) || (start < token.getStartTime() && end > token.getStartTime())) {
                    isVehicleFree = false;
                    break;
                }
            }

            if (isVehicleFree)
                result.add(vehicleID);
        }
    }

    public List<String> getAllVehiclesOfBranch(final String branchID) throws EntityNotFoundException {
        List<String> result = new ArrayList<>();

        for (String vehicleType : branchVehicleTypes.get(branchID)) {
            result.addAll(branchVehicleRepository.getAllVehicleIDsForAType(branchID, vehicleType));
        }

        return result;
    }

    private static BranchService instance;
    public static BranchService getInstance() {
        if (instance == null)
            instance = new BranchService();
        return instance;
    }

    private BranchService() {

    }

}
