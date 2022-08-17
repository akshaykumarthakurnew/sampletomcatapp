package org.springframework.samples.petclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
 
@Configuration()
@SpringBootApplication

@ComponentScan(basePackages = "org.springframework.samples", includeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "org.springframework.samples.*"))

@EnableJdbcRepositories(basePackages = { "org.springframework.samples.petclinic.repository.jdbc" })

public class PetClinicApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(PetClinicApplication.class, args);
	}

}
