package com.cog.hotel.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cog.hotel.model.AuthenticationRequest;
import com.cog.hotel.model.AuthenticationResponse;
import com.cog.hotel.model.CustomerDetails;
import com.cog.hotel.model.Reservation;
import com.cog.hotel.model.Rooms;
import com.cog.hotel.payload.DeleteRequest;
import com.cog.hotel.payload.ReservationRequest;
import com.cog.hotel.payload.SearchRequest;
import com.cog.hotel.payload.ViewRequest;
import com.cog.hotel.repository.CustomerRepo;
import com.cog.hotel.repository.ReservationRepository;
import com.cog.hotel.repository.RoomsRepository;
import com.cog.hotel.service.CustomerDetailsService;
import com.cog.hotel.service.JwtService;
import com.cog.hotel.service.ReservationService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth") // Context Root
@CrossOrigin(origins = "*")
@Slf4j
@Validated
public class MainController {

	@Autowired
	private AuthenticationManager authenticationManager; // variable

	@Autowired
	private CustomerDetailsService customerDetailsService;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private CustomerRepo customerRepo;

	@Autowired
	private RoomsRepository roomsRepository;

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private ReservationRepository reservationRepository;

	/**
	 * 
	 * @return ResponseEntity
	 */
	@GetMapping("/health-check")
	public ResponseEntity<String> healthCheck() { // for Health check [PERMITTED FOR ALL]
		return new ResponseEntity<String>("Hotel Reservation MS WORKING!!", HttpStatus.OK);
	}

	/**
	 * 
	 * @param request
	 * @return response
	 */
	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> generateJwt(@Valid @RequestBody AuthenticationRequest request) {

		AuthenticationResponse authenticationResponse = new AuthenticationResponse(0, "Invalid", "Invalid", "Invalid",
				"Invalid", "Invalid", "Invalid", false);
		ResponseEntity<AuthenticationResponse> response = null;

		// authenticating the User-Credentials
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getEmailAddress(), request.getPassword()));
			// else when it authenticates successfully
			final CustomerDetails customerDetails = customerDetailsService
					.loadUserByUsername(request.getEmailAddress());

			authenticationResponse.setCustomerId(customerDetails.getCustomerId());
			authenticationResponse.setEmailAddress(customerDetails.getEmailAddress());
			authenticationResponse.setFirstName(customerDetails.getFirstName());
			authenticationResponse.setLastName(customerDetails.getLastName());
			authenticationResponse.setPhoneNumber(customerDetails.getPhoneNumber());
			authenticationResponse.setAddress(customerDetails.getAddress());
			authenticationResponse.setJwt(jwtService.generateToken(customerDetails));
			authenticationResponse.setValid(true);

			// final String jwt = jwtService.generateToken(customerDetails); // returning
			// the token as response

			// test
			log.info("Authenticated Customer :: {}", customerDetails);

			response = new ResponseEntity<AuthenticationResponse>(authenticationResponse, HttpStatus.OK);

			log.info("Successfully Authenticated Customer!");

		} catch (Exception e) {
			log.error("{} !! info about request-body : {}", e.getMessage(), request);
			response = new ResponseEntity<AuthenticationResponse>(authenticationResponse, HttpStatus.FORBIDDEN);
		}
		log.info("-------- Exiting /authenticate");
		return response;
	}

	@PostMapping("/signup")
	public String SignUp(@RequestBody CustomerDetails customerDetails) {
		customerRepo.save(customerDetails);
		return "Customer Saved";
	}

	@GetMapping("/getAllUsers")
	public List<CustomerDetails> getAll() {
		return customerRepo.findAll();
	}

	// Room Controller

	// Search Rooms

	/**
	 * Search all rooms
	 * 
	 * @return
	 */
	@GetMapping(value = "/rooms/all")
	public ResponseEntity<?> listAllRooms() {
		List<Rooms> rooms = roomsRepository.findAll();
		return new ResponseEntity<>(rooms, HttpStatus.OK);
	}

	/**
	 * Search Available Rooms
	 * 
	 * @param searchRequest
	 * @return
	 */
	@GetMapping(value = "/rooms/available")
	public ResponseEntity<?> listAvailableRooms() {
		Iterable<Rooms> availableRooms = roomsRepository.findByBookingStatusFalse();
		return new ResponseEntity<>(availableRooms, HttpStatus.OK);
	}

	/**
	 * add reservation
	 * 
	 * @param reservationRequest
	 * @return
	 */

	@PostMapping("/rooms/reservation")
	public ResponseEntity<?> addReservation(@RequestBody ReservationRequest reservationRequest) {

		List<Rooms> updatedDetails = reservationService.addReservationService(reservationRequest);

		return new ResponseEntity<>(updatedDetails, HttpStatus.OK);

	}

	/**
	 * delete reservation by passing a reservation Id
	 * 
	 * @param reservationId
	 * @return
	 */
	@PostMapping("/rooms/deleteReservation")
	public ResponseEntity<?> deleteReservation(@RequestBody DeleteRequest deleteRequest) {

		Optional<Reservation> reservation = reservationService
				.deleteReservationService(deleteRequest.getReservationId());

		return new ResponseEntity<>(reservation, HttpStatus.OK);
	}

	/**
	 * view reservation by passing a reservation Id
	 * 
	 * @param reservationId
	 * @return
	 */

	@GetMapping("/rooms/reserved")
	public ResponseEntity<?> viewReservation(@RequestBody ViewRequest viewRequest) {

		Optional<Reservation> reservation = reservationRepository.findByCustomerId(viewRequest.getCustomerId());

		return new ResponseEntity<>(reservation, HttpStatus.OK);
	}

	/**
	 * Search all rooms
	 * 
	 * @return
	 */
	@GetMapping(value = "/rooms/allReserved")
	public ResponseEntity<?> listAllReservedRooms() {
		List<Reservation> reservation = reservationRepository.findAll();
		return new ResponseEntity<>(reservation, HttpStatus.OK);
	}

}
