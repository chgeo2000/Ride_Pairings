package com.example.ridepairings.Dto.RideDto;

import com.example.ridepairings.Dto.EmployeeDto.EmployeeResponseDto;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
public class RideResponseDto {

    private String externalId;

    private String name;

    private String description;

    private String location;

    private String rideType;

    private Integer minimumHeight;

    private Integer maximumHeight;

    private Integer maxNrOfRideAttendants;

    private Boolean isClosed;

    private Set<EmployeeResponseDto> employeeSet;
}