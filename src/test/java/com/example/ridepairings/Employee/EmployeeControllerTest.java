package com.example.ridepairings.Employee;

import com.example.ridepairings.Controller.EmployeeController.EmployeeController;
import com.example.ridepairings.Domain.Ride;
import com.example.ridepairings.Dto.EmployeeDto.EmployeeRequestDto;
import com.example.ridepairings.Dto.EmployeeDto.EmployeeResponseDto;
import com.example.ridepairings.Dto.RideDto.RideResponseDto;
import com.example.ridepairings.Ride.RideFactory;
import com.example.ridepairings.Service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {EmployeeController.class, EmployeeService.class})
class EmployeeControllerTest {

    private static final String EMPLOYEE_URL = "/employee";
    private EmployeeRequestDto employeeRequestDto;
    private EmployeeResponseDto employeeResponseDto;
    private Ride ride;
    private RideResponseDto rideResponseDto;
    private Page<EmployeeResponseDto> employeeResponseDtoPage;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        employeeRequestDto = EmployeeFactory.getEmployeeRequestDto();
        employeeResponseDto = EmployeeFactory.getEmployeeResponseDto();
        ride = RideFactory.getRide();
        rideResponseDto = RideFactory.getRideResponseDto();
        employeeResponseDtoPage = new PageImpl<>(Collections.singletonList(employeeResponseDto));
    }

    @Test
    void testCreateEmployee() throws Exception {
        when(employeeService.createEmployee(any(EmployeeRequestDto.class))).thenReturn(employeeResponseDto);

        mockMvc.perform(post(EMPLOYEE_URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequestDto))
        )

                .andExpect(status().isCreated())

                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(employeeResponseDto.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.offDay").value(employeeResponseDto.getOffDay()));

        verify(employeeService, times(1)).createEmployee(Mockito.any(EmployeeRequestDto.class));
    }

    @Test
    void testGetAllEmployees() throws Exception {
        when(employeeService.getAllEmployees(any(Pageable.class))).thenReturn(employeeResponseDtoPage);

        mockMvc.perform(get(EMPLOYEE_URL + "/get-all"))

                .andExpect(status().isOk())

                .andExpect(MockMvcResultMatchers.jsonPath("$.empty").value(false))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value(1));


        verify(employeeService, times(1)).getAllEmployees(Mockito.any(Pageable.class));
    }

    @Test
    void testGetEmployeeByExternalId() throws Exception {
        when(employeeService.getEmployeeByExternalId(any(String.class))).thenReturn(employeeResponseDto);

        mockMvc.perform(get(EMPLOYEE_URL + "/get/" + employeeResponseDto.getExternalId()))

                .andExpect(status().isOk())

                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(employeeResponseDto.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.offDay").value(employeeResponseDto.getOffDay()));

        verify(employeeService, times(1)).getEmployeeByExternalId(any(String.class));
    }

    @Test
    void testUpdateEmployee() throws Exception {
        when(employeeService.updateEmployee(any(String.class), any(EmployeeRequestDto.class))).thenReturn(employeeResponseDto);

        mockMvc.perform(put(EMPLOYEE_URL + "/update/" + employeeResponseDto.getExternalId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeRequestDto)))

                .andExpect(status().isOk())

                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(employeeResponseDto.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.offDay").value(employeeResponseDto.getOffDay()));

        verify(employeeService, times(1)).updateEmployee(any(String.class), any(EmployeeRequestDto.class));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        doNothing().when(employeeService).deleteEmployee(any(String.class));

        mockMvc.perform(delete(EMPLOYEE_URL + "/delete/" + employeeResponseDto.getExternalId()))

                .andExpect(status().isNoContent());

        verify(employeeService, times(1)).deleteEmployee(any(String.class));


    }

    @Test
    void testAddRideToEmployee() throws Exception {
        when(employeeService.addRideToEmployee(any(String.class), any(String.class))).thenReturn(employeeResponseDto);

        mockMvc.perform(patch(EMPLOYEE_URL + "/add-ride/" + employeeResponseDto.getExternalId())
                        .param("rideExternalId", ride.getExternalId()))

                .andExpect(status().isOk())

                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(employeeResponseDto.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.offDay").value(employeeResponseDto.getOffDay()));

        verify(employeeService, times(1)).addRideToEmployee(any(String.class), any(String.class));
    }

    @Test
    void testGetAllRidesWhereEmployeeWorked() throws Exception {
        when(employeeService.getAllRidesWhereEmployeeWorked(any(String.class))).thenReturn(Collections.singletonList(rideResponseDto));

        mockMvc.perform(get(EMPLOYEE_URL + "/get-rides/" + employeeResponseDto.getExternalId()))

                .andExpect(status().isOk())

                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(rideResponseDto.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(rideResponseDto.getName()));

        verify(employeeService, times(1)).getAllRidesWhereEmployeeWorked(any(String.class));
    }
}