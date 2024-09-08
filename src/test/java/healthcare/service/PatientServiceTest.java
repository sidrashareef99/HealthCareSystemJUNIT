package healthcare.service;

import healthcare.model.Patient;
import healthcare.repository.PatientRepository;
import healthcare.impl.PatientRepositoryImpl;
import healthcare.service.PatientService;
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

public class PatientServiceTest {

    private PatientService patientService;
    private SessionFactory sessionFactory;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sessionFactory = new Configuration().configure("hibernate-test.cfg.xml").buildSessionFactory();
        PatientRepository patientRepository = new PatientRepositoryImpl(sessionFactory);
        patientService = new PatientService(patientRepository);
    }


    @AfterEach
    public void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }


    @Test
    public void testCreatePatient() {
        Patient patient = new Patient();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setDateOfBirth("1980-01-01");
        patient.setEmail("john.doe@example.com");
        patient.setPhoneNumber("1234567890");

        patientService.createPatient(patient);
        assertNotNull(patient.getPatientId());
    }

    @Test
    public void testGetPatientById() {
        Patient patient = patientService.getPatientById(1);
        assertNull(patient);

        // Assuming you have a record with ID 1 in the database for testing
        // Uncomment below line if there is an existing patient in the database
        // assertNotNull(patientService.getPatientById(1));
    }

    @Test
    public void testGetAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        assertNotNull(patients);
        assertTrue(patients.isEmpty());
    }

    @Test
    public void testUpdatePatient() {
        Patient patient = new Patient();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setDateOfBirth("1980-01-01");
        patient.setEmail("john.doe@example.com");
        patient.setPhoneNumber("1234567890");

        patientService.createPatient(patient);
        patient.setPhoneNumber("0987654321");
        patientService.updatePatient(patient);

        Patient updatedPatient = patientService.getPatientById(patient.getPatientId());
        assertEquals("0987654321", updatedPatient.getPhoneNumber());
    }

    @Test
    public void testDeletePatient() {
        Patient patient = new Patient();
        patient.setFirstName("Jane");
        patient.setLastName("Doe");
        patient.setDateOfBirth("1990-02-02");
        patient.setEmail("jane.doe@example.com");
        patient.setPhoneNumber("9876543210");

        patientService.createPatient(patient);
        int id = patient.getPatientId();
        patientService.deletePatient(id);

        assertNull(patientService.getPatientById(id));
    }
    @ParameterizedTest
    @ValueSource(strings = {"john.doe@example.com", "jane.doe@example.com"})
    public void testCreatePatientWithDifferentEmails(String email) {
        Patient patient = new Patient();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setEmail(email);
        patientService.createPatient(patient);
        assertNotNull(patient.getPatientId());
        assertEquals(email, patient.getEmail());
    }

}