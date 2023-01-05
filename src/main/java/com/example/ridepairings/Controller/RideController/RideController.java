package com.example.ridepairings.Controller.RideController;

import com.example.ridepairings.Dto.EmployeeDto.EmployeeResponseDto;
import com.example.ridepairings.Dto.RideDto.RideRequestDto;
import com.example.ridepairings.Dto.RideDto.RideResponseDto;
import com.example.ridepairings.Service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(path = "/ride")
public class RideController {

    private final RideService rideService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/create")
    public RideResponseDto createRide(@RequestBody RideRequestDto rideRequestDto) {
        return rideService.createRide(rideRequestDto);
    }

    @GetMapping(path = "/get-all")
    public Page<RideResponseDto> getAllRides(Pageable pageable) {
        return rideService.getAllRides(pageable);
    }

    @GetMapping(path = "/get/{externalId}")
    public RideResponseDto getRideByExternalId(@PathVariable String externalId) {
        return rideService.getRideByExternalId(externalId);
    }

    @PutMapping(path = "/update/{externalId}")
    public RideResponseDto updateRide(@PathVariable String externalId, @RequestBody RideRequestDto rideRequestDto) {
        return rideService.updateRide(externalId, rideRequestDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/delete/{externalId}")
    public void deleteRide(@PathVariable String externalId) {
        rideService.deleteRide(externalId);
    }

    @GetMapping(path = "/get-employees/{externalId}")
    public List<EmployeeResponseDto> getAllEmployeesWhoWorkedAtRide(@PathVariable String externalId) {
        return rideService.getAllEmployeesWhoWorkedAtRide(externalId);
    }

}
