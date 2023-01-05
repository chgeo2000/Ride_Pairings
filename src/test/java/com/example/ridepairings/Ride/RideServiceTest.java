package com.example.ridepairings.Ride;

import com.example.ridepairings.Converter.EmployeeConverter;
import com.example.ridepairings.Converter.RideConverter;
import com.example.ridepairings.Domain.Employee;
import com.example.ridepairings.Domain.Ride;
import com.example.ridepairings.Dto.EmployeeDto.EmployeeResponseDto;
import com.example.ridepairings.Dto.RideDto.RideRequestDto;
import com.example.ridepairings.Dto.RideDto.RideResponseDto;
import com.example.ridepairings.Employee.EmployeeFactory;
import com.example.ridepairings.Repository.EmployeeRepository;
import com.example.ridepairings.Repository.RideRepository;
import com.example.ridepairings.Service.RideService;
import com.example.ridepairings.Service.RideServiceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RideServiceTest {
    private Ride ride;
    private RideRequestDto rideRequestDto;
    private RideResponseDto rideResponseDto;
    private Employee employee;
    private EmployeeResponseDto employeeResponseDto;
    private Pageable pageable;
    private Page<Ride> ridePage;

    @InjectMocks
    private RideService rideService;

    @Mock
    private RideRepository rideRepository;

    @Mock
    private RideConverter rideConverter;

    @Mock
    private RideServiceValidator rideServiceValidator;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeConverter employeeConverter;


    @BeforeEach
    void setUp() {
        ride = RideFactory.getRide();
        rideRequestDto = RideFactory.getRideRequestDto();
        rideResponseDto = RideFactory.getRideResponseDto();
        pageable = PageRequest.of(0, 20);
        ridePage = new PageImpl<>(Collections.singletonList(ride));
        employee = EmployeeFactory.getEmployee();
        employeeResponseDto = EmployeeFactory.getEmployeeResponseDto();
    }

    @Test
    void testCreateRide() {
        when(rideConverter.fromRequestDtoToEntity(rideRequestDto)).thenReturn(ride);
        doNothing().when(rideServiceValidator).validateRideType(ride);
        when(rideRepository.save(ride)).thenReturn(ride);
        when(rideConverter.fromEntityToResponseDto(ride)).thenReturn(rideResponseDto);

        RideResponseDto result = rideService.createRide(rideRequestDto);

        assertEquals(result.getName(), rideResponseDto.getName());
        assertEquals(result.getRideType(), rideResponseDto.getRideType());
    }

    @Test
    void testGetRideByExternalId() {
        when(rideRepository.findByExternalId(ride.getExternalId())).thenReturn(Optional.of(ride));
        when(rideConverter.fromEntityToResponseDto(ride)).thenReturn(rideResponseDto);

        RideResponseDto result = rideService.getRideByExternalId(ride.getExternalId());

        assertEquals(result.getName(), rideResponseDto.getName());
        assertEquals(result.getRideType(), rideResponseDto.getRideType());
    }

    @Test
    void testGetAllRides() {
        when(rideRepository.findAll(pageable)).thenReturn(ridePage);
        when(rideConverter.fromEntityToResponseDto(ride)).thenReturn(rideResponseDto);

        Page<RideResponseDto> result = rideService.getAllRides(pageable);

        assertEquals(result.getTotalElements(), 1);
        assertEquals(result.getContent(), Collections.singletonList(rideResponseDto));
    }

    @Test
    void testUpdateRide() {
        when(rideRepository.existsByExternalId(ride.getExternalId())).thenReturn(true);
        when(rideConverter.fromRequestDtoToEntity(rideRequestDto)).thenReturn(ride);
        when(rideRepository.save(ride)).thenReturn(ride);
        when(rideConverter.fromEntityToResponseDto(ride)).thenReturn(rideResponseDto);

        RideResponseDto result = rideService.updateRide(ride.getExternalId(), rideRequestDto);

        assertEquals(result.getName(), rideResponseDto.getName());
        assertEquals(result.getRideType(), rideResponseDto.getRideType());
    }

    @Test
    void testDeleteRide() {
        try (MockedConstruction<Employee> rideMockedConstruction = Mockito.mockConstruction(Employee.class)) {

            Employee testEmployee = EmployeeFactory.getEmployee();

            when(rideRepository.existsByExternalId(ride.getExternalId())).thenReturn(true);
            when(employeeRepository.findAll()).thenReturn(Collections.singletonList(testEmployee));
            doNothing().when(rideRepository).deleteByExternalId(ride.getExternalId());

            rideService.deleteRide(ride.getExternalId());

            verify(rideRepository, times(1)).deleteByExternalId(ride.getExternalId());
        }
    }

    @Test
    void testGetAllEmployeesWhoWorkedAtRide() {
        when(rideRepository.findByExternalId(ride.getExternalId())).thenReturn(Optional.of(ride));
        ride.setEmployees(Collections.singleton(employee));
        when(employeeConverter.fromEntityToResponseDto(employee)).thenReturn(employeeResponseDto);

        List<EmployeeResponseDto> result = rideService.getAllEmployeesWhoWorkedAtRide(ride.getExternalId());

        assertEquals(result.size(), 1);
        assertEquals(result.get(0), employeeResponseDto);
    }
}