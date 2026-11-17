package kz.damumed.model;

public class Patient extends Person {
    private String birthDate;

    public Patient(int id, String fullName, String phone, String birthDate) {
        super(id, fullName, phone);
        this.birthDate = birthDate;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
}