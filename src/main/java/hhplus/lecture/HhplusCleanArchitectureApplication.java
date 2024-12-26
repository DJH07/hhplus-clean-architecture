package hhplus.lecture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HhplusCleanArchitectureApplication {

	public static void main(String[] args) {
		SpringApplication.run(HhplusCleanArchitectureApplication.class, args);
	}

}
