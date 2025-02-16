package kr.mafoo.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableR2dbcAuditing
@ConfigurationPropertiesScan
@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
	//	BlockHound.install();
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
