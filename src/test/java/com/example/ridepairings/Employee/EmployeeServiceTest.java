package com.example.ridepairings.Employee;

import com.example.ridepairings.Converter.EmployeeConverter;
import com.example.ridepairings.Converter.RideConverter;
import com.example.ridepairings.Domain.Employee;
import com.example.ridepairings.Domain.Ride;
import com.example.ridepairings.Dto.EmployeeDto.EmployeeRequestDto;
import com.example.ridepairings.Dto.EmployeeDto.EmployeeResponseDto;
import com.example.ridepairings.Dto.RideDto.RideResponseDto;
import com.example.ridepairings.Repository.EmployeeRepository;
import com.example.ridepairings.Repository.RideRepository;
import com.example.ridepairings.Ride.RideFactory;
import com.example.ridepairings.Service.EmployeeService;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    private Employee employee;
    private EmployeeRequestDto employeeRequestDto;
    private EmployeeResponseDto employeeResponseDto;
    private Ride ride;
    private RideResponseDto rideResponseDto;
    private Page<Employee> employeePage;
    private Pageable pageable;

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeConverter employeeConverter;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private RideRepository rideRepository;

    @Mock
    private RideConverter rideConverter;

    @BeforeEach
    void setUp() {
        employee = EmployeeFactory.getEmployee();
        employeeRequestDto = EmployeeFactory.getEmployeeRequestDto();
        employeeResponseDto = EmployeeFactory.getEmployeeResponseDto();
        ride = RideFactory.getRide();
        rideResponseDto = RideFactory.getRideResponseDto();
        pageable = PageRequest.of(0, 20);
        employeePage = new PageImpl<>(Collections.singletonList(employee));
    }

    @Test
    void testCreateEmployee() {
        when(employeeConverter.fromRequestDtoToEntity(employeeRequestDto)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeConverter.fromEntityToResponseDto(employee)).thenReturn(employeeResponseDto);

        EmployeeResponseDto result = employeeService.createEmployee(employeeRequestDto);

        assertEquals(result.getBirthdate(), employeeResponseDto.getBirthdate());
        assertEquals(result.getExternalId(), employeeResponseDto.getExternalId());

    }

    @Test
    void testGetAllEmployees() {
        when(employeeRepository.findAll(pageable)).thenReturn(employeePage);
        when(employeeConverter.fromEntityToResponseDto(employee)).thenReturn(employeeResponseDto);

        Page<EmployeeResponseDto> result = employeeService.getAllEmployees(pageable);

        assertEquals(result.getTotalElements(), 1);
        assertEquals(result.getContent(), Collections.singletonList(employeeResponseDto));
    }

    @Test
    void testGetEmployeeByExternalId() {
        when(employeeRepository.findByExternalId(employee.getExternalId())).thenReturn(Optional.of(employee));
        when(employeeConverter.fromEntityToResponseDto(employee)).thenReturn(employeeResponseDto);

        EmployeeResponseDto result = employeeService.getEmployeeByExternalId(employee.getExternalId());

        assertEquals(result.getEmail(), employeeResponseDto.getEmail());
        assertEquals(result.getHourlyWage(), employeeResponseDto.getHourlyWage());
    }

    @Test
    void testUpdateEmployee() {
            when(employeeRepository.existsByExternalId(employee.getExternalId())).thenReturn(true);
            when(employeeConverter.fromRequestDtoToEntity(employeeRequestDto)).thenReturn(employee);
            when(employeeRepository.save(employee)).thenReturn(employee);
            when(employeeConverter.fromEntityToResponseDto(employee)).thenReturn(employeeResponseDto);

            EmployeeResponseDto result = employeeService.updateEmployee(employee.getExternalId(), employeeRequestDto);

            assertEquals(result.getLastName(), employeeResponseDto.getLastName());
            assertEquals(result.getFirstName(), employeeResponseDto.getFirstName());
    }

    @Test
    void testDeleteEmployee() {
        try (MockedConstruction<Ride> rideMockedConstruction = Mockito.mockConstruction(Ride.class)) {
            Ride testRide = RideFactory.getRide();

            when(employeeRepository.existsByExternalId(employee.getExternalId())).thenReturn(true);
            when(rideRepository.findAll()).thenReturn(Collections.singletonList(testRide));
            doNothing().when(employeeRepository).deleteByExternalId(employee.getExternalId());

            employeeService.deleteEmployee(employee.getExternalId());

            verify(employeeRepository, times(1)).deleteByExternalId(employee.getExternalId());
        }
    }

    @Test
    void testGetAllRidesWhereEmployeeWorked() {
        when(employeeRepository.findByExternalId(employee.getExternalId())).thenReturn(Optional.of(employee));
        when(rideConverter.fromEntityToResponseDto(ride)).thenReturn(rideResponseDto);
        employee.setRides(Collections.singleton(ride));

        List<RideResponseDto> result = employeeService.getAllRidesWhereEmployeeWorked(employee.getExternalId());

        assertEquals(result.size(), 1);
        assertEquals(result.get(0), rideResponseDto);

    }

    @Test
    void testAddRideToEmployee() {
        when(rideRepository.findByExternalId(ride.getExternalId())).thenReturn(Optional.of(ride));
        when(employeeRepository.findByExternalId(employee.getExternalId())).thenReturn(Optional.of(employee));
        employee.setRides(new HashSet<>(Collections.singletonList(ride)));
        ride.setEmployees(new HashSet<>(Collections.singletonList(employee)));
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(rideRepository.save(ride)).thenReturn(ride);
        when(employeeConverter.fromEntityToResponseDto(employee)).thenReturn(employeeResponseDto);
        employeeResponseDto.setRideSet(new HashSet<>(Collections.singletonList(rideResponseDto)));

        EmployeeResponseDto result = employeeService.addRideToEmployee(employee.getExternalId(), ride.getExternalId());

        assertEquals(result.getRideSet().size(), 1);
        assertEquals(result.getExternalId(), employeeResponseDto.getExternalId());
        assertEquals(result.getEmail(), employeeResponseDto.getEmail());
    }
}