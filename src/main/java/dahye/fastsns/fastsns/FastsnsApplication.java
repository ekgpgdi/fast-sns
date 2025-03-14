package dahye.fastsns.fastsns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FastsnsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FastsnsApplication.class, args);
    }

}
