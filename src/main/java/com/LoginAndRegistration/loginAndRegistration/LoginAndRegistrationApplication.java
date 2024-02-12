package com.LoginAndRegistration.loginAndRegistration;

import com.LoginAndRegistration.loginAndRegistration.Entity.Role;
import com.LoginAndRegistration.loginAndRegistration.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
@SpringBootApplication
public class LoginAndRegistrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginAndRegistrationApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(RoleRepository roleRepository){
		return (args) -> {
			Role role = new Role();
			role.setName("ROLE_ADMIN");
			roleRepository.save(role);
		};
	}
}
