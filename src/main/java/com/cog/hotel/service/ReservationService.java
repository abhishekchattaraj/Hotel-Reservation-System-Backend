package com.cog.hotel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cog.hotel.model.Reservation;
import com.cog.hotel.model.Rooms;
import com.cog.hotel.payload.ReservationRequest;
import com.cog.hotel.repository.ReservationRepository;
import com.cog.hotel.repository.RoomsRepository;

@Service
public class ReservationService {

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private RoomsRepository roomsRepository;

	public Optional<Reservation> deleteReservationService(int reservationId) {

		Optional<Reservation> reservation = reservationRepository.findById(reservationId);

		reservationRepository.deleteById(reservationId);

		Reservation selectedReservation = null;

		if (reservation.isPresent()) {
			selectedReservation = reservation.get();
		}

		List<Rooms> masterDB = roomsRepository.findByRoomId(selectedReservation.getRoomId());

		Rooms selectedRoom = masterDB.get(0);

		selectedRoom.setBookingStatus(false);
		selectedRoom.setCheckIn(null);
		selectedRoom.setCheckOut(null);

		roomsRepository.save(selectedRoom);

		return reservation;

	}

	public List<Rooms> addReservationService(ReservationRequest reservationRequest) {

		Reservation reservation = new Reservation();

		reservation.setCustomerId(reservationRequest.getCustomerId());
		reservation.setFromDate(reservationRequest.getFromDate());
		reservation.setToDate(reservationRequest.getToDate());
		reservation.setRoomId(reservationRequest.getRoomId());
		reservation.setRoomType(reservationRequest.getRoomType());

		reservationRepository.save(reservation);

		// Updating master table

		List<Rooms> masterDB = roomsRepository.findByRoomId(reservationRequest.getRoomId());

		Rooms selectedRoom = masterDB.get(0);

		selectedRoom.setBookingStatus(true);
		selectedRoom.setCheckIn(reservationRequest.getFromDate());
		selectedRoom.setCheckOut(reservationRequest.getToDate());

		roomsRepository.save(selectedRoom);

		return roomsRepository.findByRoomId(reservationRequest.getRoomId());

	}
}