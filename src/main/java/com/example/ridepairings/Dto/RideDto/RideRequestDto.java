package com.example.ridepairings.Dto.RideDto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Valid
public class RideRequestDto {

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String location;

    @NotNull
    private String rideType;

    @NotNull
    private Integer minimumHeight;

    private Integer maximumHeight;

    @NotNull
    private Integer maxNrOfRideAttendants;

    @NotNull
    private Boolean isClosed;
}
