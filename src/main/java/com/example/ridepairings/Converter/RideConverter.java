package com.example.ridepairings.Converter;


import com.example.ridepairings.Domain.Ride;
import com.example.ridepairings.Dto.EmployeeDto.EmployeeResponseDto;
import com.example.ridepairings.Dto.RideDto.RideRequestDto;
import com.example.ridepairings.Dto.RideDto.RideResponseDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RideConverter {
    private final ModelMapper modelMapper;

    public Ride fromRequestDtoToEntity(RideRequestDto rideRequestDto) {
        return modelMapper.map(rideRequestDto, Ride.class);
    }

    public RideResponseDto fromEntityToResponseDto(Ride ride) {
        RideResponseDto rideResponseDto = modelMapper.map(ride, RideResponseDto.class);
        rideResponseDto.setEmployeeSet(ride.getEmployees()
                .stream().map(employee -> modelMapper.map(employee, EmployeeResponseDto.class)).collect(Collectors.toSet()));
        return rideResponseDto;
    }
}