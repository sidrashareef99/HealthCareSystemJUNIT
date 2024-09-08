package healthcare.repository;

import healthcare.model.Doctor;
import java.util.List;

public interface DoctorRepository {
    void create(Doctor doctor);
    Doctor findById(int id);
    List<Doctor> findAll();
    void update(Doctor doctor);
    void delete(int id);
}