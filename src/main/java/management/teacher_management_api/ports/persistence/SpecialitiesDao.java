package management.teacher_management_api.ports.persistence;

import java.util.List;

public interface SpecialitiesDao {
    List<String> listAll();

    Long findByName(String name);
}
