package management.teacher_management_api.usercases;

import lombok.RequiredArgsConstructor;
import management.teacher_management_api.ports.persistence.SpecialitiesDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetSpecialitiesUsecase {
    private final SpecialitiesDao specialitiesDao;

    public List<String> execute() {
        return specialitiesDao.listAll();
    }
}
