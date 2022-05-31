package management.teacher_management_api.infrastructure.spring;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@ComponentScan(basePackages = {"management.teacher_management_api"})
@EntityScan(basePackages = {"management.teacher_management_api"})
public class AppConfig {
}
