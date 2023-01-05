package com.example.ridepairings.Repository;

import com.example.ridepairings.Domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByExternalId(String externalId);
    void deleteByExternalId(String externalId);

    Optional<Employee> findEmployeeByFirstNameAndLastName(String firstName, String lastName);

    Optional<Employee> findByExternalId(String externalId);
}
