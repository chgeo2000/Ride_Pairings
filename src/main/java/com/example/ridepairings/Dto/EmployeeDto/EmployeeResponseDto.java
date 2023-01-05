package com.example.ridepairings.Dto.EmployeeDto;

import com.example.ridepairings.Dto.RideDto.RideResponseDto;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
public class EmployeeResponseDto {

    private String externalId;

    private String firstName;

    private String lastName;

    private LocalDate birthdate;

    private String phoneNumber;

    private String email;

    private boolean newEmployee;

    private Integer hourlyWage;

    private String offDay;

    private Set<RideResponseDto> rideSet;
}