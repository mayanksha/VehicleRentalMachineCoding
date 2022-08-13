package com.navi.driver;

import com.navi.exception.DuplicateEntityException;
import com.navi.exception.RentalServiceException;
import com.navi.model.BookingToken;
import com.navi.service.VehicleRentalService;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class RentalSystemDriver {
    final String ADD_BRANCH = "ADD_BRANCH";
    final String BOOK = "BOOK";
    final String ADD_VEHICLE = "ADD_VEHICLE";
    final String DISPLAY_VEHICLES = "DISPLAY_VEHICLES";

    private VehicleRentalService rentalService = VehicleRentalService.getInstance();

    public void driveProgramWithInputFile() {
        InputStream inputStream = RentalSystemDriver.class.getClassLoader().getResourceAsStream("input3.txt");
        Scanner lineScanner = new Scanner(inputStream);

        while (lineScanner.hasNextLine()) {
            Scanner scanner = new Scanner(lineScanner.nextLine());
            String command = scanner.next();

            if (command.equals(ADD_BRANCH)) {
                handleAddBranch(scanner);
            } else if (command.equals(ADD_VEHICLE)) {
                handleAddVehicle(scanner);
            } else if (command.equals(BOOK)) {
                handleBooking(scanner);
            } else {
                handleDisplayAll(scanner);
            }
        }
    }

    private void handleDisplayAll(Scanner scanner) {
        String branchID = scanner.next();
        Integer start = scanner.nextInt(), end = scanner.nextInt();

        List<String> allUnbookedVehiclesInTime = rentalService.getAllUnbookedVehiclesInTimeSorted(branchID, start, end);
        log.info("{}", String.join(",", allUnbookedVehiclesInTime));
    }

    private void handleBooking(Scanner scanner) {
        String branchID = scanner.next();
        String vehicleType = scanner.next();
        Integer start = scanner.nextInt(), end = scanner.nextInt();

        try {
            log.debug("BOOKING: {}, {}, {}, {}", branchID, vehicleType, start, end);
            BookingToken bookingToken = rentalService.bookVehicleTypeInBranch(branchID, vehicleType, start, end);

            if (bookingToken == null) {
                log.info("-1");
            } else {
                log.info("{}", bookingToken.getPrice());
            }
        } catch (DuplicateEntityException e) {
            log.error("Exception occurred. message: {}", e.getMessage());
        }
    }

    private void handleAddVehicle(Scanner scanner) {
        String branchID = scanner.next();
        String vehicleType = scanner.next();
        String vehicleID = scanner.next();
        Double price = scanner.nextDouble();

        try {
            boolean possible = rentalService.addNewVehicleToBranch(branchID, vehicleType, vehicleID, price);
            log.debug("Adding vehicle: {}", possible);
            if (possible) {
                log.info("TRUE");
            } else log.info("FALSE");
        } catch (DuplicateEntityException | RentalServiceException e) {
            log.info("FALSE");
            log.debug("Exception occurred. message: {}", e.getMessage());
        }
    }

    private void handleAddBranch(Scanner scanner) {
        String branchID = scanner.next();
        String vehicleTypes = scanner.next();

        List<String> split = List.of(vehicleTypes.split(","));

        try {
            rentalService.createNewBranch(branchID);
            rentalService.addVehicleTypesToBranch(branchID, split);
            log.info("TRUE");
        } catch (DuplicateEntityException | RentalServiceException e) {
            log.info("FALSE");
            log.debug("Exception occurred. message: {}", e.getMessage());
        }
    }
}
