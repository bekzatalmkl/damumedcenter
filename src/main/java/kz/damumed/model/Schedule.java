package kz.damumed.model;

public class Schedule {
    private int id;
    private Doctor doctor;
    private String date;
    private String startTime;
    private String endTime;

    public Schedule(int id, Doctor doctor, String date,
                    String startTime, String endTime) {
        this.id = id;
        this.doctor = doctor;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}