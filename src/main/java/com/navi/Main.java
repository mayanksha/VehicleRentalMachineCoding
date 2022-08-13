package com.navi;

import com.navi.driver.RentalSystemDriver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    public static void main(String[] args) {
        RentalSystemDriver driver = new RentalSystemDriver();
        driver.driveProgramWithInputFile();
    }

}
