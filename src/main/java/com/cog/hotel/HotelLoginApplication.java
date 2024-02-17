package com.cog.hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@CrossOrigin("*")
@SpringBootApplication
@EnableSwagger2
public class HotelLoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelLoginApplication.class, args);
	}

}
