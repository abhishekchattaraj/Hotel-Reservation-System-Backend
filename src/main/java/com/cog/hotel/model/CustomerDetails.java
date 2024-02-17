package com.cog.hotel.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Component

@Data
@Entity
@Table(name = "Customer")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter

public class CustomerDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int customerId;
	@Column(unique = true)
	private String emailAddress;
	private String password;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String address;

	public CustomerDetails(CustomerDetails customerDetails) {
		this.customerId = customerDetails.getCustomerId();
		this.emailAddress = customerDetails.getEmailAddress();
		this.password = new BCryptPasswordEncoder(10).encode(customerDetails.getPassword());
		this.firstName = customerDetails.getFirstName();
		this.lastName = customerDetails.getLastName();
		this.phoneNumber = customerDetails.getPhoneNumber();
		this.address = customerDetails.getAddress();
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return new ArrayList<>();
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.emailAddress;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
