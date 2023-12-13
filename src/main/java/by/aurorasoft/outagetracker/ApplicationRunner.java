package by.aurorasoft.outagetracker;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import static org.springframework.boot.SpringApplication.run;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties
public class ApplicationRunner {
    public static void main(final String... args) {
        run(ApplicationRunner.class, args);
    }
}
