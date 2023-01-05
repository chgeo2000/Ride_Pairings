package com.example.ridepairings.Converter;

import com.example.ridepairings.Domain.Employee;
import com.example.ridepairings.Dto.EmployeeDto.EmployeeRequestDto;
import com.example.ridepairings.Dto.EmployeeDto.EmployeeResponseDto;
import com.example.ridepairings.Dto.RideDto.RideResponseDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeConverter {

    private final ModelMapper modelMapper;

    public Employee fromRequestDtoToEntity(EmployeeRequestDto employeeRequestDto) {
        return modelMapper.map(employeeRequestDto, Employee.class);
    }

    public EmployeeResponseDto fromEntityToResponseDto(Employee employee) {
        EmployeeResponseDto employeeResponseDto = modelMapper.map(employee, EmployeeResponseDto.class);

        employeeResponseDto.setRideSet(employee.getRides()
                .stream().map(ride -> modelMapper.map(ride, RideResponseDto.class)).collect(Collectors.toSet()));

        return employeeResponseDto;
    }

}
