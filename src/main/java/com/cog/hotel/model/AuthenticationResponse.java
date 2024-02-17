package com.cog.hotel.model;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class AuthenticationResponse {
	private int customerId;
	private String emailAddress;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String address;
	private String jwt;
	private boolean isValid;
}