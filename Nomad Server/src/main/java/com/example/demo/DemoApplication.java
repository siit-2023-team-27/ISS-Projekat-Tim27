package com.example.demo;

import Controllers.AccommodationController;
import config.WebSecurityConfig;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import security.auth.RestAuthEntryPoint;
import util.TokenUtils;

@SpringBootApplication()
@ComponentScan(basePackageClasses = {AccommodationController.class, WebSecurityConfig.class})
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
