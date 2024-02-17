package com.cog.hotel.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class AuthenticationRequest {

	@NotEmpty(message = "Email Address cannot be Empty.")
	private String emailAddress;
	@NotEmpty(message = "Password cannot be Empty.")
	@Pattern(regexp = "[a-zA-Z0-9]{0,20}", message = "Password cannot be a special character and length cannot be more than 20")
	private String password;
}
