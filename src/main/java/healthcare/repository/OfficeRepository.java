package healthcare.repository;

import healthcare.model.Office;
import java.util.List;

public interface OfficeRepository {
    void create(Office office);
    Office findById(int id);
    List<Office> findAll();
    void update(Office office);
    void delete(int id);
}