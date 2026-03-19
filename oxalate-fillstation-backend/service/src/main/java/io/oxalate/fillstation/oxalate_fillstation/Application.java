package io.oxalate.fillstation.oxalate_fillstation;

import io.oxalate.fillstation.oxalate_fillstation.service.InitialAdminService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner initialAdminRunner(InitialAdminService initialAdminService) {
		return args -> {
			String adminEmail = System.getenv("INITIAL_ADMIN_EMAIL");
			String adminPassword = System.getenv("INITIAL_ADMIN_PASSWORD");
			initialAdminService.createInitialAdminIfNeeded(adminEmail, adminPassword);
		};
	}
}

