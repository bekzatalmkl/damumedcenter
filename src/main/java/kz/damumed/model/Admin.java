package kz.damumed.model;

public class Admin extends Person {
    private String username;
    private String password;

    public Admin(int id, String fullName, String phone,
                 String username, String password) {
        super(id, fullName, phone);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}