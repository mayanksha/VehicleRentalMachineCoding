package com.navi;

import com.navi.driver.RentalSystemDriver;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;

@Slf4j
public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        String filePath = args[0];

        RentalSystemDriver driver = new RentalSystemDriver(filePath);
        driver.driveProgramWithInputFile();
    }

}
