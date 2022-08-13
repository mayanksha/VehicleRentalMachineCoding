package com.navi.service;

import com.navi.exception.DuplicateEntityException;
import com.navi.exception.EntityNotFoundException;
import com.navi.exception.RentalServiceException;
import com.navi.model.BookingToken;
import com.navi.model.Vehicle;
import com.navi.repository.BookingRepository;
import com.navi.strategy.VehicleSelectionStrategy;
import com.navi.strategy.impl.LowestPriceVehicleSelectionStrategy;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class VehicleRentalService {

    private BranchService branchService = BranchService.getInstance();
    private VehicleService vehicleService = VehicleService.getInstance();
    private BookingRepository bookingRepository = BookingRepository.getInstance();

    /**
     * Allow the selection strategy to be changed based upon what the Admin chooses
     */
    @Setter
    private VehicleSelectionStrategy vehicleSelectionStrategy;

    public void createNewBranch(final String name) throws DuplicateEntityException {
        branchService.createAndInsertNewBranch(name, UUID.randomUUID().toString());
    }

    public void addVehicleTypesToBranch(final String branchID, List<String> vehicleTypes) throws RentalServiceException {
        branchService.addVehicleTypesToBranch(branchID, vehicleTypes);
    }

    public boolean addNewVehicleToBranch(final String branchID, final String vehicleType, final String vehicleID, final Double price) throws DuplicateEntityException, RentalServiceException {
        if (!branchService.checkValidVehicleTypeForBranch(branchID, vehicleType)) {
            return false;
        }

        // firstly, we'll have to create a new vehicle altogether
        Vehicle vehicle = vehicleService.createAndInsertNewVehicle(vehicleType, vehicleID, price);
        branchService.addVehicleToBranch(branchID, vehicle);
        return true;
    }

    /**
     * Used to book a particular vehicle type from a branch
     * @param branchID
     * @param vehicleType
     * @param start
     * @param end
     * @return Returns a Booking token if successful else null
     * @throws DuplicateEntityException
     */
    public BookingToken bookVehicleTypeInBranch(final String branchID, final String vehicleType, Integer start, Integer end) throws DuplicateEntityException {
        List<String> allVehiclesOfSpecificType;

        try {
            allVehiclesOfSpecificType = branchService.getAllUnbookedVehiclesOfTypeInRange(branchID, vehicleType, start, end);
            log.debug("Available vehicles. branchID: {}, type: {}, vehicles: {}", branchID, vehicleType, allVehiclesOfSpecificType);
        } catch (EntityNotFoundException e) {
            return null;
        }

        if (allVehiclesOfSpecificType == null || allVehiclesOfSpecificType.isEmpty()) {
            return null;
        }

        Map.Entry<String, Double> entry = vehicleSelectionStrategy.pickVehicleAndGetBookingPrice(allVehiclesOfSpecificType);

        BookingToken bookingToken = new BookingToken(branchID, entry.getKey(), entry.getValue() * (end - start), start, end);
        bookingRepository.insertBookingForId(bookingToken.getID(), bookingToken);

        return bookingToken;
    }

    public List<String> getAllUnbookedVehiclesInTimeSorted(final String branchID, Integer start, Integer end) {
        List<String> allUnbookedVehiclesInRange = branchService.getAllUnbookedVehiclesInRange(branchID, start, end);

        List<Vehicle> vehicles = new ArrayList<>();
        for (String vehicleID : allUnbookedVehiclesInRange) {
            try {
                vehicles.add(vehicleService.getVehicleForID(vehicleID));
            } catch (EntityNotFoundException e) {
                continue;
            }
        }

        Collections.sort(vehicles);

        return vehicles.stream().map(e -> e.getID()).collect(Collectors.toList());
    }

    private static VehicleRentalService instance;

    public static VehicleRentalService getInstance() {
        if (instance == null) {
            instance = new VehicleRentalService();
        }
        return instance;
    }

    private VehicleRentalService() {
        this.vehicleSelectionStrategy = LowestPriceVehicleSelectionStrategy.getInstance();
    }

}
