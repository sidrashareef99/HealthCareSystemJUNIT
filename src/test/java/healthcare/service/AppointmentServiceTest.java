package healthcare.service;

import healthcare.model.Appointment;
import healthcare.model.Doctor;
import healthcare.model.Patient;
import healthcare.repository.AppointmentRepository;
import healthcare.impl.AppointmentRepositoryImpl;
import healthcare.repository.DoctorRepository;
import healthcare.impl.DoctorRepositoryImpl;
import healthcare.repository.PatientRepository;
import healthcare.impl.PatientRepositoryImpl;
import healthcare.service.AppointmentService;
import healthcare.service.DoctorService;
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

public class AppointmentServiceTest {

    private AppointmentService appointmentService;
    private DoctorService doctorService;
    private PatientService patientService;
    private SessionFactory sessionFactory;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sessionFactory = new Configuration().configure("hibernate-test.cfg.xml").buildSessionFactory();

        // Repositories
        AppointmentRepository appointmentRepository = new AppointmentRepositoryImpl(sessionFactory);
        DoctorRepository doctorRepository = new DoctorRepositoryImpl(sessionFactory);
        PatientRepository patientRepository = new PatientRepositoryImpl(sessionFactory);

        // Services
        appointmentService = new AppointmentService(appointmentRepository);
        doctorService = new DoctorService(doctorRepository);
        patientService = new PatientService(patientRepository);
    }

    @AfterEach
    public void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void testCreateAppointment() {
        // Create and save the patient
        Patient patient = new Patient();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patientService.createPatient(patient);

        // Create and save the doctor
        Doctor doctor = new Doctor();
        doctor.setFirstName("Jane");
        doctor.setLastName("Smith");
        doctorService.createDoctor(doctor);

        // Create the appointment and set the patient and doctor
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate("2024-09-01");
        appointment.setNotes("Annual checkup");

        // Save the appointment
        appointmentService.createAppointment(appointment);

        // Assert that the appointment has been saved with an ID
        assertNotNull(appointment.getAppointmentId());
    }



    @Test
    public void testGetAppointmentById() {
        Appointment appointment = appointmentService.getAppointmentById(1);
        assertNull(appointment);

        // Uncomment this line if you have a real Appointment with ID 1 in the database for testing
        // assertNotNull(appointmentService.getAppointmentById(1));
    }

    @Test
    public void testGetAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        assertNotNull(appointments);
        assertTrue(appointments.isEmpty());
    }

    @Test
    public void testUpdateAppointment() {
        // Create and save the patient
        Patient patient = new Patient();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patientService.createPatient(patient);

        // Create and save the doctor
        Doctor doctor = new Doctor();
        doctor.setFirstName("Jane");
        doctor.setLastName("Smith");
        doctorService.createDoctor(doctor);

        // Create the appointment and set the patient and doctor
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate("2024-09-02");
        appointment.setNotes("Initial notes");

        // Save the appointment
        appointmentService.createAppointment(appointment);

        // Update the appointment
        appointment.setNotes("Updated notes");
        appointmentService.updateAppointment(appointment);

        // Verify the updated notes
        Appointment updatedAppointment = appointmentService.getAppointmentById(appointment.getAppointmentId());
        assertEquals("Updated notes", updatedAppointment.getNotes());
    }

    @Test
    public void testDeleteAppointment() {
        // Create and save the patient
        Patient patient = new Patient();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patientService.createPatient(patient);

        // Create and save the doctor
        Doctor doctor = new Doctor();
        doctor.setFirstName("Jane");
        doctor.setLastName("Smith");
        doctorService.createDoctor(doctor);

        // Create the appointment and set the patient and doctor
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate("2024-09-03");
        appointment.setNotes("To be deleted");

        // Save the appointment
        appointmentService.createAppointment(appointment);

        // Delete the appointment
        int id = appointment.getAppointmentId();
        appointmentService.deleteAppointment(id);

        // Assert that the appointment has been deleted
        assertNull(appointmentService.getAppointmentById(id));
    }


    @ParameterizedTest
    @ValueSource(strings = {"2024-09-01", "2024-09-02", "2024-09-03"})
    public void testCreateAppointmentWithDifferentDates(String date) {
        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(date);
        appointment.setNotes("Checkup");

        appointmentService.createAppointment(appointment);
        assertNotNull(appointment.getAppointmentId());
        assertEquals(date, appointment.getAppointmentDate());
    }
}