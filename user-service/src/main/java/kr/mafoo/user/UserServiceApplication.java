package kr.mafoo.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
	//	BlockHound.install();
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
