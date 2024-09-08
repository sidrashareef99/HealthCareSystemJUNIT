package healthcare.service;

import healthcare.model.Doctor;
import healthcare.repository.DoctorRepository;
import java.util.List;

public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public void createDoctor(Doctor doctor) {
        doctorRepository.create(doctor);
    }

    public Doctor getDoctorById(int id) {
        return doctorRepository.findById(id);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public void updateDoctor(Doctor doctor) {
        doctorRepository.update(doctor);
    }

    public void deleteDoctor(int id) {
        doctorRepository.delete(id);
    }
}