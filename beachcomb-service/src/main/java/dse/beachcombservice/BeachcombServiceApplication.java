package dse.beachcombservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
public class BeachcombServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeachcombServiceApplication.class, args);
	}

}
