package kz.damumed.storage;

import kz.damumed.model.Appointment;
import kz.damumed.model.MedicalRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvFileStorage implements FileStorage {

    private final String fileName;

    public CsvFileStorage(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void saveAppointments(List<Appointment> appointments)
            throws IOException {

        try (PrintWriter writer = new PrintWriter(
                new FileWriter(fileName))) {

            writer.println("AppointmentID,PatientID,PatientName,Phone,BirthDate,DoctorID,DoctorName,DoctorPhone,SpecialtyID,SpecialtyName,Date,Time,Status");

            for (Appointment a : appointments) {
                writer.println(
                    a.getId() + "," +
                    a.getPatient().getId() + "," +
                    a.getPatient().getFullName() + "," +
                    a.getPatient().getPhone() + "," +
                    a.getPatient().getBirthDate() + "," +
                    a.getDoctor().getId() + "," +
                    a.getDoctor().getFullName() + "," +
                    a.getDoctor().getPhone() + "," +
                    a.getDoctor().getSpecialty().getId() + "," +
                    a.getDoctor().getSpecialty().getName() + "," +
                    a.getDate() + "," +
                    a.getTime() + "," +
                    a.getStatus()
                );
            }
        }
    }

    @Override
    public List<String[]> loadAppointments() throws IOException {
        List<String[]> records = new ArrayList<>();
        File file = new File(fileName);

        if (!file.exists()) {
            return records;
        }

        try (BufferedReader reader = new BufferedReader(
                new FileReader(fileName))) {

            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    records.add(line.split(",", -1));
                }
            }
        }

        return records;
    }

    @Override
    public void saveMedicalRecords(List<MedicalRecord> records)
            throws IOException {

        try (PrintWriter writer = new PrintWriter(
                new FileWriter("medical_records.csv"))) {

            writer.println("ID,PatientID,PatientName,DoctorID,DoctorName,Diagnosis,Date,Notes");

            for (MedicalRecord r : records) {
                writer.println(
                    r.getId() + "," +
                    r.getPatient().getId() + "," +
                    r.getPatient().getFullName() + "," +
                    r.getDoctor().getId() + "," +
                    r.getDoctor().getFullName() + "," +
                    r.getDiagnosis() + "," +
                    r.getDate() + "," +
                    r.getNotes()
                );
            }
        }
    }

    @Override
    public List<String[]> loadMedicalRecords() throws IOException {
        List<String[]> records = new ArrayList<>();
        File file = new File("medical_records.csv");

        if (!file.exists()) {
            return records;
        }

        try (BufferedReader reader = new BufferedReader(
                new FileReader(file))) {

            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    records.add(line.split(",", -1));
                }
            }
        }

        return records;
    }
}