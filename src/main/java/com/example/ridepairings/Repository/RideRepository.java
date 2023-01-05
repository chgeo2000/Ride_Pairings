package com.example.ridepairings.Repository;

import com.example.ridepairings.Domain.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    boolean existsByExternalId(String externalId);

    void deleteByExternalId(String externalId);

    Optional<Ride> findByExternalId(String externalId);

    List<Ride> findAllByIsClosedFalse();
}
