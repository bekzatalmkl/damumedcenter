package kz.damumed.storage;

import java.io.IOException;
import java.util.List;
import kz.damumed.model.Appointment;
import kz.damumed.model.MedicalRecord;

public interface FileStorage {

    void saveAppointments(List<Appointment> appointments)
            throws IOException;

    List<String[]> loadAppointments() throws IOException;

    void saveMedicalRecords(List<MedicalRecord> records)
            throws IOException;

    List<String[]> loadMedicalRecords() throws IOException;
}