package healthcare.service;

import healthcare.model.Doctor;
import healthcare.repository.DoctorRepository;
import healthcare.impl.DoctorRepositoryImpl;
import healthcare.service.DoctorService;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DoctorServiceTest {

    private DoctorService doctorService;
    private SessionFactory sessionFactory;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sessionFactory = new Configuration().configure("hibernate-test.cfg.xml").buildSessionFactory();
        DoctorRepository doctorRepository = new DoctorRepositoryImpl(sessionFactory);
        doctorService = new DoctorService(doctorRepository);
    }

    @AfterEach
    public void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void testCreateDoctor() {
        Doctor doctor = new Doctor();
        doctor.setFirstName("Jane");
        doctor.setLastName("Smith");
        doctor.setSpecialty("Cardiology");
        doctor.setEmail("jane.smith@example.com");

        doctorService.createDoctor(doctor);
        assertNotNull(doctor.getDoctorId());
    }

    @Test
    public void testGetDoctorById() {
        Doctor doctor = doctorService.getDoctorById(1);
        assertNull(doctor);

        // Uncomment if a doctor exists in the database for ID 1
        // assertNotNull(doctorService.getDoctorById(1));
    }

    @Test
    public void testGetAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        assertNotNull(doctors);
        assertTrue(doctors.isEmpty());
    }

    @Test
    public void testUpdateDoctor() {
        Doctor doctor = new Doctor();
        doctor.setFirstName("Jane");
        doctor.setLastName("Smith");
        doctor.setSpecialty("Cardiology");
        doctor.setEmail("jane.smith@example.com");

        doctorService.createDoctor(doctor);
        doctor.setSpecialty("Neurology");
        doctorService.updateDoctor(doctor);

        Doctor updatedDoctor = doctorService.getDoctorById(doctor.getDoctorId());
        assertEquals("Neurology", updatedDoctor.getSpecialty());
    }

    @Test
    public void testDeleteDoctor() {
        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setSpecialty("Dermatology");
        doctor.setEmail("john.doe@example.com");

        doctorService.createDoctor(doctor);
        int id = doctor.getDoctorId();
        doctorService.deleteDoctor(id);

        assertNull(doctorService.getDoctorById(id));
    }


    @ParameterizedTest
    @ValueSource(strings = {"Cardiology", "Neurology", "Dermatology"})
    public void testCreateDoctorWithDifferentSpecialties(String specialty) {
        Doctor doctor = new Doctor();
        doctor.setFirstName("Jane");
        doctor.setLastName("Smith");
        doctor.setSpecialty(specialty);
        doctor.setEmail("jane.smith@example.com");

        doctorService.createDoctor(doctor);
        assertNotNull(doctor.getDoctorId());
        assertEquals(specialty, doctor.getSpecialty());
    }
}