package kr.mafoo.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import reactor.blockhound.BlockHound;

@ConfigurationPropertiesScan
@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
	//	BlockHound.install();
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
