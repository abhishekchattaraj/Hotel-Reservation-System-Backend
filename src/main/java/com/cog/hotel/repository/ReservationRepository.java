package com.cog.hotel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cog.hotel.model.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

	Optional<Reservation> findByCustomerId(int customerId);

}