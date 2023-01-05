package com.example.ridepairings.Employee;

import com.example.ridepairings.Domain.Employee;
import com.example.ridepairings.Dto.EmployeeDto.EmployeeRequestDto;
import com.example.ridepairings.Dto.EmployeeDto.EmployeeResponseDto;

import java.time.LocalDate;

public class EmployeeFactory {

    private static final String EXTERNAL_ID = "78807ba4-b8f4-4514-bbe7-b88c103886a8";
    private static final String FIRST_NAME = "George";
    private static final String LAST_NAME = "Chira";
    private static final LocalDate BIRTHDATE = LocalDate.parse("2000-11-13");
    private static final String PHONE_NUMBER = "0770910617";
    private static final String EMAIL = "chirageorgea@gmail.com";
    private static final boolean NEW_EMPLOYEE = true;
    private static final Integer HOURLY_WAGE = 14;
    private static final String OFF_DAY = "Saturday";

    public static Employee getEmployee() {
        return Employee.builder()
                .externalId(EXTERNAL_ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .birthdate(BIRTHDATE)
                .phoneNumber(PHONE_NUMBER)
                .email(EMAIL)
                .newEmployee(NEW_EMPLOYEE)
                .hourlyWage(HOURLY_WAGE)
                .offDay(OFF_DAY)
                .build();
    }

    public static EmployeeRequestDto getEmployeeRequestDto() {
        return EmployeeRequestDto.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .birthdate(BIRTHDATE)
                .phoneNumber(PHONE_NUMBER)
                .email(EMAIL)
                .newEmployee(NEW_EMPLOYEE)
                .hourlyWage(HOURLY_WAGE)
                .offDay(OFF_DAY)
                .build();
    }

    public static EmployeeResponseDto getEmployeeResponseDto() {
        return EmployeeResponseDto.builder()
                .externalId(EXTERNAL_ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .birthdate(BIRTHDATE)
                .phoneNumber(PHONE_NUMBER)
                .email(EMAIL)
                .newEmployee(NEW_EMPLOYEE)
                .hourlyWage(HOURLY_WAGE)
                .offDay(OFF_DAY)
                .build();
    }
}
