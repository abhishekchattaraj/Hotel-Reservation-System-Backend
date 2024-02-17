package com.cog.hotel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cog.hotel.model.*;

@Repository
public interface RoomsRepository extends JpaRepository<Rooms, Integer> {

	@Query
	public Iterable<Rooms> findByBookingStatusFalse();

	@Query
	public Iterable<Rooms> findByBookingStatusTrue();

	public Iterable<Rooms> findByBookingStatusAndRoomType(boolean b, String string);

	public List<Rooms> findByRoomId(int roomId);
}