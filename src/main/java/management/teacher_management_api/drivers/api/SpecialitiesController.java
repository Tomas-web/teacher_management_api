package management.teacher_management_api.drivers.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import management.teacher_management_api.usercases.GetSpecialitiesUsecase;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@CrossOrigin
@Controller
@RequiredArgsConstructor
public class SpecialitiesController {
    private final GetSpecialitiesUsecase getSpecialitiesUsecase;

    @GetMapping(path = "/specialities")
    public ResponseEntity<List<String>> getSpecialities() {
        try {
            val result = getSpecialitiesUsecase.execute();
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            log.error("Unhandled exception", ex);
            return ResponseEntity.internalServerError().build();
        }
    }
}
