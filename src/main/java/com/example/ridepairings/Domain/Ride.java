package com.example.ridepairings.Domain;

import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "ride")
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "external_id", nullable = false, updatable = false, unique = true)
    private String externalId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "ride_type", nullable = false)
    private String rideType;

    @Column(name = "minimum_height", nullable = false)
    private Integer minimumHeight;

    @Column(name = "maximum_height")
    private Integer maximumHeight;

    @Column(name = "maximum_number_of_ride_attendants", nullable = false)
    private Integer maxNrOfRideAttendants;

    @Column(name = "is_closed", nullable = false)
    private Boolean isClosed;

    @ManyToMany
    @JoinTable(name = "ride_employee",
            joinColumns = @JoinColumn(name = "ride_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private Set<Employee> employees = new LinkedHashSet<>();
}