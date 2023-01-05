package com.example.ridepairings.Controller.EmployeeController;

import com.example.ridepairings.Dto.EmployeeDto.EmployeeRequestDto;
import com.example.ridepairings.Dto.EmployeeDto.EmployeeResponseDto;
import com.example.ridepairings.Dto.RideDto.RideResponseDto;
import com.example.ridepairings.Service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@RequestMapping("/employee")
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeController {

    private final EmployeeService employeeService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/create")
    public EmployeeResponseDto createEmployee(@Valid @RequestBody EmployeeRequestDto employeeRequestDto) {
        return employeeService.createEmployee(employeeRequestDto);
    }

    @GetMapping(path = "/get-all")
    public Page<EmployeeResponseDto> getAllEmployees(Pageable pageable) {
        return employeeService.getAllEmployees(pageable);
    }

    @GetMapping(path = "/get/{externalId}")
    public EmployeeResponseDto getEmployeeByExternalId(@PathVariable String externalId) {
        return employeeService.getEmployeeByExternalId(externalId);
    }

    @PutMapping(path = "/update/{externalId}")
    public EmployeeResponseDto updateEmployee(@PathVariable String externalId, @Valid @RequestBody EmployeeRequestDto updatedEmployee) {
        return employeeService.updateEmployee(externalId, updatedEmployee);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/delete/{externalId}")
    public void deleteEmployee(@PathVariable String externalId) {
        employeeService.deleteEmployee(externalId);
    }

    @GetMapping("/get-all-available")
    public List<EmployeeResponseDto> getAllAvailableEmployeesForGivenDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate aDate) {
        return employeeService.getAllAvailableEmployeesForGivenDate(aDate);
    }

    @PatchMapping(path = "/add-ride/{employeeExternalId}")
    public EmployeeResponseDto addRideToEmployee(@PathVariable String employeeExternalId, @RequestParam String rideExternalId) {
        return employeeService.addRideToEmployee(employeeExternalId, rideExternalId);
    }

    @GetMapping(path = "/get-rides/{externalId}")
    public List<RideResponseDto> getAllRidesWhereEmployeeWorked(@PathVariable String externalId) {
        return employeeService.getAllRidesWhereEmployeeWorked(externalId);
    }

    @GetMapping(path = "/pair-employees-rides")
    public Map<String, List<String>> pairEmployeesWithRides(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate aDate) {
        return employeeService.pairEmployeesWithRides(aDate);
    }
}
