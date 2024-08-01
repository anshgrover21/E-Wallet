package org.majorProject.UserService;

import org.majorProject.UserService.Model.User;
import org.majorProject.UserService.Model.UserType;
import org.majorProject.UserService.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class UserServiceApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		User user = User.builder().name("trx-service").password(passwordEncoder.encode("trx-service")).phoneNo("trx-service").authorities("SERVICE").userType(UserType.SERVICE).build();

//		User user = User.builder().
//				phoneNo("txn-service")."
//				password(passwordEncoder.encode("txn-service")).
//				authority("SERVICE").userType(UserType.SERVICE).
//				build();
//		userRepo.save(user);

	}

}
