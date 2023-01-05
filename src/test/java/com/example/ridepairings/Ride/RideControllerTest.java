package com.example.ridepairings.Ride;

import com.example.ridepairings.Controller.RideController.RideController;
import com.example.ridepairings.Dto.EmployeeDto.EmployeeResponseDto;
import com.example.ridepairings.Dto.RideDto.RideRequestDto;
import com.example.ridepairings.Dto.RideDto.RideResponseDto;
import com.example.ridepairings.Employee.EmployeeFactory;
import com.example.ridepairings.Service.RideService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RideController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {RideController.class, RideService.class})
class RideControllerTest {

    private static final String RIDE_URL = "/ride";

    private RideRequestDto rideRequestDto;
    private RideResponseDto rideResponseDto;
    private Page<RideResponseDto> rideResponseDtoPage;
    private EmployeeResponseDto employeeResponseDto;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RideService rideService;

    @BeforeEach
    void setUp() {
        rideRequestDto = RideFactory.getRideRequestDto();
        rideResponseDto = RideFactory.getRideResponseDto();
        rideResponseDtoPage = new PageImpl<>(Collections.singletonList(rideResponseDto));
        employeeResponseDto = EmployeeFactory.getEmployeeResponseDto();
    }

    @Test
    void testCreateRide() throws Exception {
        when(rideService.createRide(any(RideRequestDto.class))).thenReturn(rideResponseDto);

        mockMvc.perform(post(RIDE_URL + "/create")

                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rideRequestDto)))

                .andExpect(status().isCreated())

                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(rideResponseDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(rideResponseDto.getDescription()));

        verify(rideService, times(1)).createRide(any(RideRequestDto.class));
    }

    @Test
    void testGetAllRides() throws Exception {
        when(rideService.getAllRides(any(Pageable.class))).thenReturn(rideResponseDtoPage);

        mockMvc.perform(get(RIDE_URL + "/get-all"))

                .andExpect(status().isOk())

                .andExpect(MockMvcResultMatchers.jsonPath("$.empty").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1));

        verify(rideService, times(1)).getAllRides(any(Pageable.class));
    }

    @Test
    void testGetRideByExternalId() throws Exception {
        when(rideService.getRideByExternalId(any(String.class))).thenReturn(rideResponseDto);

        mockMvc.perform(get(RIDE_URL + "/get/" + rideResponseDto.getExternalId()))

                .andExpect(status().isOk())

                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(rideResponseDto.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(rideResponseDto.getName()));

        verify(rideService, times(1)).getRideByExternalId(any(String.class));
    }

    @Test
    void testUpdateRide() throws Exception {
        when(rideService.updateRide(any(String.class), any(RideRequestDto.class))).thenReturn(rideResponseDto);

        mockMvc.perform(put(RIDE_URL + "/update/" + rideResponseDto.getExternalId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rideRequestDto)))

                .andExpect(status().isOk())

                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(rideResponseDto.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(rideResponseDto.getName()));

        verify(rideService, times(1)).updateRide(any(String.class), any(RideRequestDto.class));
    }

    @Test
    void testDeleteRide() throws Exception {
        doNothing().when(rideService).deleteRide(any(String.class));

        mockMvc.perform(delete(RIDE_URL + "/delete/" + rideResponseDto.getExternalId()))

                .andExpect(status().isNoContent());

        verify(rideService, times(1)).deleteRide(any(String.class));
    }

    @Test
    void testGetAllEmployeesWhoWorkedAtRide() throws Exception {
        when(rideService.getAllEmployeesWhoWorkedAtRide(any(String.class))).thenReturn(Collections.singletonList(employeeResponseDto));

        mockMvc.perform(get(RIDE_URL + "/get-employees/" + rideResponseDto.getExternalId()))

                .andExpect(status().isOk())

                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value(employeeResponseDto.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].offDay").value(employeeResponseDto.getOffDay()));

        verify(rideService, times(1)).getAllEmployeesWhoWorkedAtRide(any(String.class));
    }
}