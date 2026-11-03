package dev.eder.ferreteria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling 
public class FerreteriaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FerreteriaApiApplication.class, args);
	}

}
