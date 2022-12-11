package management.teacher_management_api;

import management.teacher_management_api.infrastructure.spring.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableConfigurationProperties({FileStorageProperties.class})
@SpringBootApplication(scanBasePackages = {"management.teacher_management_api"})
public class TeacherManagementApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeacherManagementApiApplication.class, args);
    }

    @PostConstruct
    public void init() {
        // Setting Spring Boot SetTimeZone
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
