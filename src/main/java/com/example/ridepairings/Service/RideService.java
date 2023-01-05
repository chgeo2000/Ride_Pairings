package com.example.ridepairings.Service;

import com.example.ridepairings.Converter.EmployeeConverter;
import com.example.ridepairings.Converter.RideConverter;
import com.example.ridepairings.Domain.Employee;
import com.example.ridepairings.Domain.Ride;
import com.example.ridepairings.Dto.EmployeeDto.EmployeeResponseDto;
import com.example.ridepairings.Dto.RideDto.RideRequestDto;
import com.example.ridepairings.Dto.RideDto.RideResponseDto;
import com.example.ridepairings.Exception.RideException.RideNotCreated;
import com.example.ridepairings.Exception.RideException.RideNotDeleted;
import com.example.ridepairings.Exception.RideException.RideNotFound;
import com.example.ridepairings.Exception.RideException.RideNotUpdated;
import com.example.ridepairings.Repository.EmployeeRepository;
import com.example.ridepairings.Repository.RideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RideService {

    private final RideRepository rideRepository;
    private final RideConverter rideConverter;
    private final EmployeeRepository employeeRepository;
    private final EmployeeConverter employeeConverter;

    private final RideServiceValidator rideServiceValidator;

    public RideResponseDto createRide(RideRequestDto rideRequestDto) {
        try {
            Ride ride = rideConverter.fromRequestDtoToEntity(rideRequestDto);
            rideServiceValidator.validateRideType(ride);
            ride.setExternalId(UUID.randomUUID().toString());
            return rideConverter.fromEntityToResponseDto(rideRepository.save(ride));
        } catch (Exception e) {
            throw new RideNotCreated("ride could not be created");
        }
    }

    public RideResponseDto getRideByExternalId(String externalId) {
        Ride ride = rideRepository.findByExternalId(externalId)
                .orElseThrow(() -> new RideNotFound("ride with external id: " + externalId + " could not be found"));

        return rideConverter.fromEntityToResponseDto(ride);
    }

    public Page<RideResponseDto> getAllRides(Pageable pageable) {
        return rideRepository.findAll(pageable).map(rideConverter::fromEntityToResponseDto);
    }

    public RideResponseDto updateRide(String externalId, RideRequestDto updatedRide) {
        try {
            if (rideRepository.existsByExternalId(externalId)) {
                return rideConverter.fromEntityToResponseDto(
                        rideRepository.save(rideConverter.fromRequestDtoToEntity(updatedRide))
                );
            } else {
                throw new RideNotFound("ride with external id " + externalId + " could not be found");
            }
        } catch (Exception e) {
            throw new RideNotUpdated("ride with external id " + externalId + " could not be updated");
        }
    }
    @Transactional
    public void deleteRide(String externalId) {
        try {
            if (rideRepository.existsByExternalId(externalId)) {
                for (Employee employee: employeeRepository.findAll()) {
                    for (Ride ride: employee.getRides()) {
                        if (ride.getExternalId().equals(externalId)) {
                            employee.getRides().remove(ride);
                            break;
                        }
                    }
                }
                rideRepository.deleteByExternalId(externalId);
            } else {
                throw new RideNotFound("ride with external id: " + externalId + " could not be found");
            }
        } catch (Exception e) {
            throw new RideNotDeleted("ride with external id " + externalId + " could not be deleted");
        }
    }

    public List<EmployeeResponseDto> getAllEmployeesWhoWorkedAtRide (String externalId) {
        Ride ride = rideRepository.findByExternalId(externalId)
                .orElseThrow(() -> new RideNotFound("ride with externalId " + externalId + " could not be found"));

        return ride.getEmployees()
                .stream()
                .map(employeeConverter::fromEntityToResponseDto).collect(Collectors.toList());
    }

    public Integer getNumberOfEmployeesRequiredForRide(Ride ride) {
        String rideType = ride.getRideType();

        switch(rideType) {
            case "Kiddie":
                return 1;
            case "Family":
                return 2;
            case "Thrill":
                return 4;
            default:
                return -1;
        }
    }
}
