package lia.unrn.edu.ar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

	@Configuration
	@ComponentScan
	@EnableAutoConfiguration
	public class ServiciosApplication {
		public static void main(String[] args) {
			SpringApplication.run(ServiciosApplication.class, args);
		}
	}
