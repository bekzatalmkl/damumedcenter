package kz.damumed.service;

import kz.damumed.model.*;
import kz.damumed.exception.AppointmentException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppointmentServiceTest {

    @Test
    void shouldBookAppointmentSuccessfully() throws AppointmentException {

        AppointmentService service = new AppointmentService();

        Specialty specialty = new Specialty(1, "Therapist");

        Doctor doctor = new Doctor(
                1, "Ali Nurlanov", "+77001112233", specialty
        );

        Patient patient = new Patient(
                1, "Test Patient", "+77001234567", "2005-01-01"
        );

        service.addDoctor(doctor);
        service.addPatient(patient);

        service.addSchedule(new Schedule(
                1, doctor, "2026-11-18", "09:00", "18:00"
        ));

        service.bookAppointment(
                1, 1, 1, "2026-11-18", "10:00"
        );

        assertEquals(1, service.getAppointments().size());
    }

    @Test
    void shouldRejectAppointmentOutsideWorkingHours() {

        AppointmentService service = new AppointmentService();

        Specialty specialty = new Specialty(1, "Therapist");

        Doctor doctor = new Doctor(
                1, "Ali Nurlanov", "+77001112233", specialty
        );

        Patient patient = new Patient(
                1, "Test Patient", "+77001234567", "2005-01-01"
        );

        service.addDoctor(doctor);
        service.addPatient(patient);

        service.addSchedule(new Schedule(
                1, doctor, "2026-11-18", "09:00", "18:00"
        ));

        assertThrows(AppointmentException.class, () ->
                service.bookAppointment(
                        1, 1, 1, "2026-11-18", "19:00"
                )
        );
    }
}