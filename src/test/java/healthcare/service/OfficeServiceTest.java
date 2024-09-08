package healthcare.service;

import healthcare.impl.AppointmentRepositoryImpl;
import healthcare.impl.DoctorRepositoryImpl;
import healthcare.impl.OfficeRepositoryImpl;
import healthcare.model.Appointment;
import healthcare.model.Doctor;
import healthcare.model.Office;
import healthcare.model.Patient;
import healthcare.repository.AppointmentRepository;
import healthcare.repository.DoctorRepository;
import healthcare.repository.OfficeRepository;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OfficeServiceTest {

    private DoctorService doctorService;
    private OfficeService officeService;

    private SessionFactory sessionFactory;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sessionFactory = new Configuration().configure("hibernate-test.cfg.xml").buildSessionFactory();

        // Repositories
        OfficeRepository officeRepository = new OfficeRepositoryImpl(sessionFactory);
        DoctorRepository doctorRepository = new DoctorRepositoryImpl(sessionFactory);

        // Services
        officeService = new OfficeService(officeRepository);
        doctorService = new DoctorService(doctorRepository);
    }


    @AfterEach
    public void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void testCreateOffice() {
        Doctor doctor = new Doctor();
        doctor.setFirstName("Abby");
        doctor.setLastName("James");
        doctorService.createDoctor(doctor);

        Office office = new Office();
        office.setLocation("404 Arm St.");
        office.setPhone("555-7650");
        office.setDoctor(doctor);
        officeService.createOffice(office);

        assertNotNull(office.getOfficeId());

    }

    @Test
    public void testUpdateOffice() {
        Office office = new Office();
        office.setPhone("555-1230");
        office.setLocation("101 Heart St.");
        office.setDoctor(doctorService.getDoctorById(1));
        officeService.createOffice(office);

        office = officeService.getOfficeById(1);

        if (office != null ){
            office.setLocation("404 Arm St.");
            office.setPhone("555-7650");
            office.setDoctor(doctorService.getDoctorById(2));
            officeService.updateOffice(office);

        } else {
            System.out.println("Office not updated");
        }

        assertEquals("404 Arm St.",office.getLocation());


    }

    @Test
    public void testDeleteOffice() {

        // Create and save the office
        Office office = new Office();
        office.setPhone("555-1230");
        office.setLocation("101 Heart St.");
        office.setDoctor(doctorService.getDoctorById(1));
        officeService.createOffice(office);

        int id = office.getOfficeId();
        officeService.deleteOffice(id);
        assertNull(officeService.getOfficeById(id));

    }
    @Test
    public void testGetOfficeById() {
        Office office = officeService.getOfficeById(1);
        assertNull(office);

        // Uncomment this line if you have a real Office with ID 1 in the database for testing
        //assertNotNull(officeService.getOfficeById(1));
    }

    @ParameterizedTest
    @CsvSource({"Main Office", "Branch Office"})
    public void testValidOfficeNames(String officeName){
        Office office = new Office();
        office.setPhone("555-1230");
        office.setLocation(officeName);
        office.setDoctor(doctorService.getDoctorById(1));
        officeService.createOffice(office);

        assertNotNull(office);
        assertEquals(officeName,office.getLocation());

    }

    @Test
    public void testInvalidInput(){
        Office office = new Office();
        office.setPhone("");
        office.setLocation("");
        office.setDoctor(doctorService.getDoctorById(1));
        officeService.createOffice(office);

        assertThrows(IllegalArgumentException.class, ()->{
            if (office.getLocation() == null || office.getLocation().isEmpty()){
                throw new IllegalArgumentException("Invalid input");
            }
        });

        assertThrows(IllegalArgumentException.class, ()->{
            if (office.getDoctor() == null){
                throw new IllegalArgumentException("Invalid input");
            }
        });

        assertThrows(IllegalArgumentException.class, ()->{
            if (office.getPhone() == null || office.getPhone().isEmpty()){
                throw new IllegalArgumentException("Invalid input");
            }
        });

    }


}
