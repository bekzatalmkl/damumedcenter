package kz.damumed;

import kz.damumed.model.*;
import kz.damumed.service.AppointmentService;
import kz.damumed.storage.CsvFileStorage;
import kz.damumed.storage.FileStorage;
import kz.damumed.exception.AppointmentException;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        AppointmentService service = new AppointmentService();
        FileStorage storage = new CsvFileStorage("data/appointments.csv");

        Admin admin = new Admin(
        1,
        "System Administrator",
        "+77000000000",
        "admin",
        "admin123"
  );

  System.out.println("\n===== ADMIN LOGIN =====");

System.out.print("Enter username: ");
String username = scanner.nextLine();

System.out.print("Enter password: ");
String password = scanner.nextLine();

if (!admin.getUsername().equals(username)
        || !admin.checkPassword(password)) {

    System.out.println("Invalid username or password!");

    scanner.close();
    return;
}

System.out.println("Admin login successful!");

        // Load saved appointments
        try {
            service.loadAppointmentsFromFile(storage);
            System.out.println("Previous data loaded successfully!");
            service.loadMedicalRecordsFromFile(storage);
System.out.println("Medical records loaded successfully!");
        } catch (IOException | RuntimeException e) {
            System.out.println("Could not load saved data: " + e.getMessage());
        }

        // Add default doctor if no doctors exist
        if (service.getDoctors().isEmpty()) {
            Specialty specialty = new Specialty(1, "Therapist");

            Doctor doctor = new Doctor(
                    1,
                    "Ali Nurlanov",
                    "+77001112233",
                    specialty
            );

            service.addDoctor(doctor);
        }
        for (Doctor d : service.getDoctors()) {
    Schedule schedule = new Schedule(
            d.getId(),
            d,
            "2026-11-18",
            "09:00",
            "18:00"
    );

    service.addSchedule(schedule);
}

        int nextPatientId = service.getNextPatientId();
        int nextAppointmentId = service.getNextAppointmentId();

        System.out.println("================================");
        System.out.println("       DAMUMED SYSTEM");
        System.out.println("  Clinic Appointment Booking");
        System.out.println("================================");

        while (true) {

            System.out.println("\n1. Register patient");
            System.out.println("2. Show doctors");
            System.out.println("3. Book appointment");
            System.out.println("4. Show appointments");
            System.out.println("5. Save appointments");
System.out.println("6. Add medical record");
System.out.println("7. Show medical records");
System.out.println("0. Exit");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {

                    case "1":
                        System.out.print("Enter full name: ");
                        String name = scanner.nextLine();

                        System.out.print("Enter phone: ");
                        String phone = scanner.nextLine();

                        System.out.print("Enter birth date: ");
                        String birthDate = scanner.nextLine();

                        Patient patient = new Patient(
                                nextPatientId++,
                                name,
                                phone,
                                birthDate
                        );

                        service.addPatient(patient);

                        System.out.println("Patient registered successfully!");
                        System.out.println("Patient ID: " + patient.getId());
                        break;

                    case "2":
                        System.out.println("\nAvailable doctors:");

                        for (Doctor d : service.getDoctors()) {
                            System.out.println(
                                    d.getId() + ". " +
                                    d.getFullName() + " - " +
                                    d.getSpecialty().getName()
                            );
                        }
                        break;

                    case "3":
                        System.out.print("Enter patient ID: ");
                        int patientId = Integer.parseInt(scanner.nextLine());

                        System.out.print("Enter doctor ID: ");
                        int doctorId = Integer.parseInt(scanner.nextLine());

                        System.out.print("Enter date (YYYY-MM-DD): ");
                        String date = scanner.nextLine();

                        System.out.print("Enter time (HH:MM): ");
                        String time = scanner.nextLine();

                        service.bookAppointment(
                                nextAppointmentId++,
                                patientId,
                                doctorId,
                                date,
                                time
                        );

                        System.out.println("Appointment booked successfully!");
                        break;

                    case "4":
                        System.out.println("\nAppointments:");

                        for (Appointment a : service.getAppointments()) {
                            System.out.println(
                                    "ID: " + a.getId() +
                                    " | Patient: " + a.getPatient().getFullName() +
                                    " | Doctor: " + a.getDoctor().getFullName() +
                                    " | Date: " + a.getDate() +
                                    " | Time: " + a.getTime() +
                                    " | Status: " + a.getStatus()
                            );
                        }
                        break;

                    case "5":
                         storage.saveAppointments(service.getAppointments());
                         storage.saveMedicalRecords(service.getMedicalRecords());
                        System.out.println("All data saved successfully!");
                        break;
                    case "6":
    System.out.print("Enter patient ID: ");
    int recordPatientId = Integer.parseInt(scanner.nextLine());

    Patient recordPatient = null;

    for (Patient p : service.getPatients()) {
        if (p.getId() == recordPatientId) {
            recordPatient = p;
            break;
        }
    }

    if (recordPatient == null) {
        System.out.println("Patient not found!");
        break;
    }

    System.out.print("Enter doctor ID: ");
    int recordDoctorId = Integer.parseInt(scanner.nextLine());

    Doctor recordDoctor = null;

    for (Doctor d : service.getDoctors()) {
        if (d.getId() == recordDoctorId) {
            recordDoctor = d;
            break;
        }
    }

    if (recordDoctor == null) {
        System.out.println("Doctor not found!");
        break;
    }

    System.out.print("Enter diagnosis: ");
    String diagnosis = scanner.nextLine();

    System.out.print("Enter date: ");
    String recordDate = scanner.nextLine();

    System.out.print("Enter notes: ");
    String notes = scanner.nextLine();

    MedicalRecord record = new MedicalRecord(
            service.getMedicalRecords().size() + 1,
            recordPatient,
            recordDoctor,
            diagnosis,
            recordDate,
            notes
    );

    service.addMedicalRecord(record);

    System.out.println("Medical record added successfully!");
    break;
    case "7":
    System.out.println("\nMedical Records:");

    if (service.getMedicalRecords().isEmpty()) {
        System.out.println("No medical records found!");
    } else {
        for (MedicalRecord r : service.getMedicalRecords()) {
            System.out.println(
                    "ID: " + r.getId() +
                    " | Patient: " + r.getPatient().getFullName() +
                    " | Doctor: " + r.getDoctor().getFullName() +
                    " | Diagnosis: " + r.getDiagnosis() +
                    " | Date: " + r.getDate() +
                    " | Notes: " + r.getNotes()
            );
        }
    }
    break;

                    case "0":
                        storage.saveAppointments(service.getAppointments());
                        storage.saveMedicalRecords(service.getMedicalRecords());

                        System.out.println("All data saved successfully!");
                        System.out.println("Thank you for using DAMUMED!");

                        scanner.close();
                         return;

                    default:
                        System.out.println("Invalid option. Try again.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");

            } catch (AppointmentException e) {
                System.out.println("Error: " + e.getMessage());

            } catch (IOException e) {
                System.out.println("File error: " + e.getMessage());
            }
        }
    }
}