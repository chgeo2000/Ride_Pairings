package com.example.ridepairings.Ride;

import com.example.ridepairings.Domain.Ride;
import com.example.ridepairings.Dto.RideDto.RideRequestDto;
import com.example.ridepairings.Dto.RideDto.RideResponseDto;

public class RideFactory {

    private static final String EXTERNAL_ID = "2ar0113d-c3f9-4b96-853c-92fb792d70b3";
    private static final String NAME = "Monster";
    private static final String DESCRIPTION = "funniest ride from the park";
    private static final String LOCATION = "north of the park next to Iowa bar";
    private static final String RIDE_TYPE = "thrill ride";
    private static final Integer MINIMUM_HEIGHT = 140;
    private static final Integer MAXIMUM_HEIGHT = 200;
    private static final Integer MAXIMUM_NR_OF_RIDE_ATTENDANTS = 4;

    public static Ride getRide() {

        return Ride.builder()
                .externalId(EXTERNAL_ID)
                .name(NAME)
                .description(DESCRIPTION)
                .location(LOCATION)
                .rideType(RIDE_TYPE)
                .minimumHeight(MINIMUM_HEIGHT)
                .maximumHeight(MAXIMUM_HEIGHT)
                .maxNrOfRideAttendants(MAXIMUM_NR_OF_RIDE_ATTENDANTS)
                .build();
    }

    public static RideRequestDto getRideRequestDto() {
        return RideRequestDto.builder()
                .name(NAME)
                .description(DESCRIPTION)
                .location(LOCATION)
                .rideType(RIDE_TYPE)
                .minimumHeight(MINIMUM_HEIGHT)
                .maximumHeight(MAXIMUM_HEIGHT)
                .maxNrOfRideAttendants(MAXIMUM_NR_OF_RIDE_ATTENDANTS)
                .build();
    }

    public static RideResponseDto getRideResponseDto() {
        return RideResponseDto.builder()
                .externalId(EXTERNAL_ID)
                .name(NAME)
                .description(DESCRIPTION)
                .location(LOCATION)
                .rideType(RIDE_TYPE)
                .minimumHeight(MINIMUM_HEIGHT)
                .maximumHeight(MAXIMUM_HEIGHT)
                .maxNrOfRideAttendants(MAXIMUM_NR_OF_RIDE_ATTENDANTS)
                .build();
    }

}