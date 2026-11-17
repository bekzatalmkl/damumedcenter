package kz.damumed.service;

import kz.damumed.model.*;
import kz.damumed.exception.AppointmentException;
import kz.damumed.storage.FileStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentService {

    private final List<Appointment> appointments = new ArrayList<>();
    private final List<MedicalRecord> medicalRecords = new ArrayList<>();
    private final List<Schedule> schedules = new ArrayList<>();

    private final Map<Integer, Patient> patients = new HashMap<>();
    private final Map<Integer, Doctor> doctors = new HashMap<>();

    public void addPatient(Patient patient) {
        patients.put(patient.getId(), patient);
    }

    public void addDoctor(Doctor doctor) {
        doctors.put(doctor.getId(), doctor);
    }

    public void addSchedule(Schedule schedule) {
        schedules.add(schedule);
    }

    public void bookAppointment(int id, int patientId,
                                int doctorId, String date,
                                String time)
            throws AppointmentException {

        Patient patient = patients.get(patientId);
        Doctor doctor = doctors.get(doctorId);

        if (patient == null) {
            throw new AppointmentException("Patient not found!");
        }

        if (doctor == null) {
            throw new AppointmentException("Doctor not found!");
        }

        boolean available = false;

        for (Schedule schedule : schedules) {
            if (schedule.getDoctor().getId() == doctorId
                    && schedule.getDate().equals(date)
                    && time.compareTo(schedule.getStartTime()) >= 0
                    && time.compareTo(schedule.getEndTime()) < 0) {

                available = true;
                break;
            }
        }

        if (!available) {
            throw new AppointmentException(
                    "Doctor is not available at this time!");
        }

        for (Appointment appointment : appointments) {
            if (appointment.getDoctor().getId() == doctorId
                    && appointment.getDate().equals(date)
                    && appointment.getTime().equals(time)
                    && appointment.getStatus().equals("CONFIRMED")) {

                throw new AppointmentException(
                        "This appointment time is already booked!");
            }
        }

        appointments.add(
                new Appointment(id, patient, doctor, date, time)
        );
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public List<Patient> getPatients() {
        return new ArrayList<>(patients.values());
    }

    public List<Doctor> getDoctors() {
        return new ArrayList<>(doctors.values());
    }

    public void addMedicalRecord(MedicalRecord record) {
        medicalRecords.add(record);
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public void loadAppointmentsFromFile(FileStorage storage)
            throws IOException {

        appointments.clear();
        patients.clear();
        doctors.clear();

        List<String[]> records = storage.loadAppointments();

        for (String[] row : records) {

            if (row.length == 6) {

                int appointmentId = Integer.parseInt(row[0]);

                Patient patient = new Patient(
                        appointmentId,
                        row[1],
                        "Not provided",
                        "Not provided"
                );

                Specialty specialty = new Specialty(1, "Unknown");

                Doctor doctor = new Doctor(
                        1,
                        row[2],
                        "Not provided",
                        specialty
                );

                Appointment appointment = new Appointment(
                        appointmentId, patient, doctor, row[3], row[4]
                );

                appointment.setStatus(row[5]);

                patients.put(patient.getId(), patient);
                doctors.put(doctor.getId(), doctor);
                appointments.add(appointment);

            } else if (row.length >= 13) {

                Specialty specialty = new Specialty(
                        Integer.parseInt(row[8]), row[9]
                );

                Patient patient = new Patient(
                        Integer.parseInt(row[1]),
                        row[2], row[3], row[4]
                );

                Doctor doctor = new Doctor(
                        Integer.parseInt(row[5]),
                        row[6], row[7], specialty
                );

                Appointment appointment = new Appointment(
                        Integer.parseInt(row[0]),
                        patient,
                        doctor,
                        row[10],
                        row[11]
                );

                appointment.setStatus(row[12]);

                patients.put(patient.getId(), patient);
                doctors.put(doctor.getId(), doctor);
                appointments.add(appointment);
            }
        }
    }

    public void loadMedicalRecordsFromFile(FileStorage storage)
            throws IOException {

        medicalRecords.clear();

        List<String[]> records = storage.loadMedicalRecords();

        for (String[] row : records) {

            if (row.length >= 8) {

                int patientId = Integer.parseInt(row[1]);
                int doctorId = Integer.parseInt(row[3]);

                Patient patient = patients.get(patientId);

                if (patient == null) {
                    patient = new Patient(
                            patientId,
                            row[2],
                            "Not provided",
                            "Not provided"
                    );

                    patients.put(patientId, patient);
                }

                Doctor doctor = doctors.get(doctorId);

                if (doctor == null) {
                    Specialty specialty = new Specialty(1, "Unknown");

                    doctor = new Doctor(
                            doctorId,
                            row[4],
                            "Not provided",
                            specialty
                    );

                    doctors.put(doctorId, doctor);
                }

                MedicalRecord record = new MedicalRecord(
                        Integer.parseInt(row[0]),
                        patient,
                        doctor,
                        row[5],
                        row[6],
                        row[7]
                );

                medicalRecords.add(record);
            }
        }
    }

    public int getNextPatientId() {
        return patients.keySet().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0) + 1;
    }

    public int getNextAppointmentId() {
        return appointments.stream()
                .mapToInt(Appointment::getId)
                .max()
                .orElse(0) + 1;
    }
}