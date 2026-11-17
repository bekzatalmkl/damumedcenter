package kz.damumed.model;

public class Doctor extends Person {
    private Specialty specialty;

    public Doctor(int id, String fullName, String phone, Specialty specialty) {
        super(id, fullName, phone);
        this.specialty = specialty;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }
}