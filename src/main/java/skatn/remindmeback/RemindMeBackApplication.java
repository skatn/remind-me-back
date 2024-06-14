package skatn.remindmeback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ConfigurationPropertiesScan
public class RemindMeBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(RemindMeBackApplication.class, args);
    }

}
