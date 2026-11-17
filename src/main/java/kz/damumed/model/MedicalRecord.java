package kz.damumed.model;

public class MedicalRecord {
    private int id;
    private Patient patient;
    private Doctor doctor;
    private String diagnosis;
    private String date;
    private String notes;

    public MedicalRecord(int id, Patient patient, Doctor doctor,
                         String diagnosis, String date, String notes) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.diagnosis = diagnosis;
        this.date = date;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getDate() {
        return date;
    }

    public String getNotes() {
        return notes;
    }
}