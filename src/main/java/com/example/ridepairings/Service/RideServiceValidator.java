package com.example.ridepairings.Service;

import com.example.ridepairings.Domain.Ride;
import com.example.ridepairings.Exception.RideException.RideTypeNotValid;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class RideServiceValidator {

    public void validateRideType(Ride ride) {
        List<String> validTypes = Arrays.asList("Kiddie", "Thrill", "Family");

        if (!validTypes.contains(ride.getRideType())) {
            throw new RideTypeNotValid(ride.getRideType() + " is not a valid ride type. Try again!");
        }
    }
}
