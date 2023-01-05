package com.example.ridepairings.Service;

import com.example.ridepairings.Converter.EmployeeConverter;
import com.example.ridepairings.Converter.RideConverter;
import com.example.ridepairings.Domain.Employee;
import com.example.ridepairings.Domain.Ride;
import com.example.ridepairings.Dto.EmployeeDto.EmployeeRequestDto;
import com.example.ridepairings.Dto.EmployeeDto.EmployeeResponseDto;
import com.example.ridepairings.Dto.RideDto.RideResponseDto;
import com.example.ridepairings.Exception.EmployeeException.EmployeeNotCreated;
import com.example.ridepairings.Exception.EmployeeException.EmployeeNotDeleted;
import com.example.ridepairings.Exception.EmployeeException.EmployeeNotFound;
import com.example.ridepairings.Exception.EmployeeException.EmployeeNotUpdated;
import com.example.ridepairings.Exception.RideException.RideNotFound;
import com.example.ridepairings.Repository.EmployeeRepository;
import com.example.ridepairings.Repository.RideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeConverter employeeConverter;
    private final RideRepository rideRepository;
    private final RideConverter rideConverter;

    private final RideService rideService;

    public EmployeeResponseDto createEmployee(EmployeeRequestDto employeeRequestDto) {
        try {

            Employee employee = employeeConverter.fromRequestDtoToEntity(employeeRequestDto);
            employee.setExternalId(UUID.randomUUID().toString());

            return employeeConverter.fromEntityToResponseDto(employeeRepository.save(employee));

        } catch (Exception e) {
            throw new EmployeeNotCreated("employee could not be created");
        }
    }

    public Page<EmployeeResponseDto> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable)
                .map(employeeConverter::fromEntityToResponseDto);
    }

    public EmployeeResponseDto getEmployeeByExternalId(String externalId) {
        return employeeConverter.fromEntityToResponseDto(
                employeeRepository.findByExternalId(externalId).
                        orElseThrow(() -> new EmployeeNotFound("employee with external id: " + externalId + " could not be found")));
    }

    public EmployeeResponseDto updateEmployee(String externalId, EmployeeRequestDto updatedEmployee) {
        try {
            if (employeeRepository.existsByExternalId(externalId)) {
                return employeeConverter.fromEntityToResponseDto(
                        employeeRepository.save(employeeConverter.fromRequestDtoToEntity(updatedEmployee)));
            } else {
                throw new EmployeeNotFound("employee with external id: " + externalId + " doesn't exist.");
            }

        } catch (Exception e) {
            throw new EmployeeNotUpdated("employee with external id: " + externalId + " could not be updated.");
        }
    }
    @Transactional
    public void deleteEmployee(String externalId) {
        try {
            if (employeeRepository.existsByExternalId(externalId)) {
                for (Ride ride: rideRepository.findAll()) {
                    for (Employee employee: ride.getEmployees()) {
                        if (employee.getExternalId().equals(externalId)) {
                            ride.getEmployees().remove(employee);
                            break;
                        }
                    }
                }
                employeeRepository.deleteByExternalId(externalId);
            } else {
                throw new EmployeeNotFound("employee with external id: " + externalId + " doesn't exist.");
            }

        } catch (Exception e) {
            throw new EmployeeNotDeleted("employee with external id: " + externalId + " could not be deleted.");
        }
    }

    public List<RideResponseDto> getAllRidesWhereEmployeeWorked(String externalId) {
        Employee employee = employeeRepository.findByExternalId(externalId)
                .orElseThrow(() -> new EmployeeNotFound("employee with external id: " + externalId + " could not be found"));

        return employee.getRides().stream().map(rideConverter::fromEntityToResponseDto).collect(Collectors.toList());
    }

    public EmployeeResponseDto addRideToEmployee (String employeeExternalId, String rideExternalId) {
        Ride ride = rideRepository.findByExternalId(rideExternalId)
                .orElseThrow(() -> new RideNotFound("ride with external id: " + rideExternalId + " could not be found"));

        Employee employee = employeeRepository.findByExternalId(employeeExternalId)
                .orElseThrow(() -> new EmployeeNotFound("employee with external id: " + employeeExternalId + " could not be found"));

        employee.getRides().add(ride);
        ride.getEmployees().add(employee);
        employeeRepository.save(employee);
        rideRepository.save(ride);

        return employeeConverter.fromEntityToResponseDto(employee);
    }

    public Map<String, List<String>> pairEmployeesWithRides(LocalDate aDate) {
        Map<String, List<String>> ridePairings = new LinkedHashMap<>();
        List<EmployeeResponseDto> availableEmployees = this.getAllAvailableEmployeesForGivenDate(aDate);

        for (Ride ride: rideRepository.findAllByIsClosedFalse()) {
            List<String> names = this.getSuitableEmployeesForRide(ride.getExternalId(), availableEmployees);
            ridePairings.put(ride.getName(), names);

            for (String name: names) {
                EmployeeResponseDto assignedEmployee = this.getEmployeeByFirstNameAndLastName(name.split(" ")[0], name.split(" ")[1]);
                availableEmployees.removeIf(availableEmployee -> availableEmployee.getExternalId().equals(assignedEmployee.getExternalId()));
            }
        }

        return ridePairings;
    }

    public List<String> getSuitableEmployeesForRide(String rideExternalId, List<EmployeeResponseDto> availableEmployees) {
        Ride ride = rideRepository.findByExternalId(rideExternalId)
                .orElseThrow(() -> new RideNotFound("ride with external id " + rideExternalId + " could not be found"));

        List<EmployeeResponseDto> employeesWhoWorkedAtLeastOnceAtRide = rideService.getAllEmployeesWhoWorkedAtRide(rideExternalId);

        switch (ride.getRideType()) {
            case "Kiddie":
                return this.getSuitableEmployeeForKiddieRide(availableEmployees);
            case "Family":
                return this.getSuitableEmployeesForFamilyRide(availableEmployees);
            case "Thrill":
                return this.getSuitableEmployeesForThrillRide(availableEmployees, employeesWhoWorkedAtLeastOnceAtRide);
            default:
                return Collections.emptyList();
        }

    }

    public List<EmployeeResponseDto> getAllAvailableEmployeesForGivenDate(LocalDate aDate) {
        String searchedDate = aDate.getDayOfWeek().name();

        return employeeRepository.findAll().stream()
                .filter(e -> !(e.getOffDay().equalsIgnoreCase(searchedDate)))
                .map(employeeConverter::fromEntityToResponseDto)
                .collect(Collectors.toList());
    }

    public EmployeeResponseDto getEmployeeByFirstNameAndLastName(String firstName, String lastName) {
        return employeeConverter.fromEntityToResponseDto(employeeRepository.findEmployeeByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new EmployeeNotFound("employee with first name: " + firstName + " and last name: " + lastName + " could not be found.")));
    }

    private List<String> getSuitableEmployeeForKiddieRide(List<EmployeeResponseDto> availableEmployees) {
        List<EmployeeResponseDto> newEmployees = this.getNewEmployees(availableEmployees);

        Random rand = new Random();

        if (newEmployees.size() != 0) {
            EmployeeResponseDto employee = newEmployees.get(rand.nextInt(newEmployees.size()));
            return Collections.singletonList(employee.getFirstName() + " " + employee.getLastName());
        }

        EmployeeResponseDto employee = availableEmployees.get(rand.nextInt(availableEmployees.size()));
        return Collections.singletonList(employee.getFirstName() + " " + employee.getLastName());
    }

    private List<String> getSuitableEmployeesForFamilyRide(List<EmployeeResponseDto> availableEmployees) {
        List<EmployeeResponseDto> newEmployees = this.getNewEmployees(availableEmployees);

        Random rand = new Random();

        EmployeeResponseDto firstEmployee;
        if (newEmployees.size() != 0) {
            firstEmployee = newEmployees.get(rand.nextInt(newEmployees.size()));
        } else {
            firstEmployee = availableEmployees.get(rand.nextInt(availableEmployees.size()));
        }
        String firstEmployeeName = firstEmployee.getFirstName() + " " + firstEmployee.getLastName();

        EmployeeResponseDto secondEmployee = availableEmployees.get(rand.nextInt(availableEmployees.size()));
        while (secondEmployee.getExternalId().equals(firstEmployee.getExternalId())) {
            secondEmployee = availableEmployees.get(rand.nextInt(availableEmployees.size()));
        }
        String secondEmployeeName = secondEmployee.getFirstName() + " " + secondEmployee.getLastName();

        return new ArrayList<>(Arrays.asList(firstEmployeeName, secondEmployeeName));
    }

    private List<String> getSuitableEmployeesForThrillRide(List<EmployeeResponseDto> availableEmployees, List<EmployeeResponseDto> employeesWhoWorkedAtRide) {
        List<String> employeesNames = new ArrayList<>();

        Random rand = new Random();

        List<EmployeeResponseDto> experiencedEmployees = this.getExperiencedEmployees(availableEmployees);

        List<String> employeesWhoWorkedAtRideExternalIds = employeesWhoWorkedAtRide.stream()
                .map(EmployeeResponseDto::getExternalId).
                collect(Collectors.toList());

        EmployeeResponseDto firstEmployee = experiencedEmployees.get(rand.nextInt(experiencedEmployees.size()));
        while(!employeesWhoWorkedAtRideExternalIds.contains(firstEmployee.getExternalId())) {
            firstEmployee = experiencedEmployees.get(rand.nextInt(experiencedEmployees.size()));
        }
        String firstEmployeeName = firstEmployee.getFirstName() + " " + firstEmployee.getLastName();
        employeesNames.add(firstEmployeeName);

        int secondEmployeeIndex = rand.nextInt(experiencedEmployees.size());
        EmployeeResponseDto secondEmployee = experiencedEmployees.get(secondEmployeeIndex);
        while (secondEmployee.getExternalId().equals(firstEmployee.getExternalId())) {
            secondEmployeeIndex = rand.nextInt(experiencedEmployees.size());
            secondEmployee = experiencedEmployees.get(secondEmployeeIndex);
        }
        String secondEmployeeName = secondEmployee.getFirstName() + " " + secondEmployee.getLastName();
        employeesNames.add(secondEmployeeName);

        int thirdEmployeeIndex = rand.nextInt(availableEmployees.size());
        EmployeeResponseDto thirdEmployee = availableEmployees.get(thirdEmployeeIndex);
        while (thirdEmployee.getExternalId().equals(firstEmployee.getExternalId()) ||
                thirdEmployee.getExternalId().equals(secondEmployee.getExternalId())) {
            thirdEmployeeIndex = rand.nextInt(availableEmployees.size());
            thirdEmployee = availableEmployees.get(thirdEmployeeIndex);
        }
        String thirdEmployeeName = thirdEmployee.getFirstName() + " " + thirdEmployee.getLastName();
        employeesNames.add(thirdEmployeeName);

        int fourthEmployeeIndex = rand.nextInt(availableEmployees.size());
        EmployeeResponseDto fourthEmployee = availableEmployees.get(fourthEmployeeIndex);
        while (fourthEmployee.getExternalId().equals(firstEmployee.getExternalId()) ||
                fourthEmployee.getExternalId().equals(secondEmployee.getExternalId()) ||
                fourthEmployee.getExternalId().equals(thirdEmployee.getExternalId())) {
            fourthEmployeeIndex = rand.nextInt(availableEmployees.size());
            fourthEmployee = availableEmployees.get(fourthEmployeeIndex);
        }
        String fourthEmployeeName = fourthEmployee.getFirstName() + " " + fourthEmployee.getLastName();
        employeesNames.add(fourthEmployeeName);

        return employeesNames;
    }

    private List<EmployeeResponseDto> getNewEmployees(List<EmployeeResponseDto> availableEmployees) {
        return availableEmployees.stream()
                .filter(EmployeeResponseDto::isNewEmployee)
                .collect(Collectors.toList());
    }

    private List<EmployeeResponseDto> getExperiencedEmployees(List<EmployeeResponseDto> availableEmployees) {
        return availableEmployees.stream()
                .filter(e -> !e.isNewEmployee())
                .collect(Collectors.toList());
    }
}