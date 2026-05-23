package hospital.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Appointment {

    public enum Status { SCHEDULED, COMPLETED, CANCELLED, NO_SHOW }

    private int appointmentId;
    private int patientId;
    private int doctorId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String reason;
    private Status status;
    private String notes;
    private double feeCharged;

    
    private String patientName;
    private String doctorName;
    private String doctorSpecialization;

    public Appointment() { this.status = Status.SCHEDULED; }

    public Appointment(int patientId, int doctorId, LocalDate date,
                       LocalTime time, String reason, double feeCharged) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = date;
        this.appointmentTime = time;
        this.reason = reason;
        this.status = Status.SCHEDULED;
        this.feeCharged = feeCharged;
    }

    public int getAppointmentId()               { return appointmentId; }
    public void setAppointmentId(int id)        { this.appointmentId = id; }
    public int getPatientId()                   { return patientId; }
    public void setPatientId(int id)            { this.patientId = id; }
    public int getDoctorId()                    { return doctorId; }
    public void setDoctorId(int id)             { this.doctorId = id; }
    public LocalDate getAppointmentDate()       { return appointmentDate; }
    public void setAppointmentDate(LocalDate d) { this.appointmentDate = d; }
    public LocalTime getAppointmentTime()       { return appointmentTime; }
    public void setAppointmentTime(LocalTime t) { this.appointmentTime = t; }
    public String getReason()                   { return reason; }
    public void setReason(String reason)        { this.reason = reason; }
    public Status getStatus()                   { return status; }
    public void setStatus(Status status)        { this.status = status; }
    public String getNotes()                    { return notes; }
    public void setNotes(String notes)          { this.notes = notes; }
    public double getFeeCharged()               { return feeCharged; }
    public void setFeeCharged(double fee)       { this.feeCharged = fee; }
    public String getPatientName()              { return patientName; }
    public void setPatientName(String n)        { this.patientName = n; }
    public String getDoctorName()               { return doctorName; }
    public void setDoctorName(String n)         { this.doctorName = n; }
    public String getDoctorSpecialization()     { return doctorSpecialization; }
    public void setDoctorSpecialization(String s){ this.doctorSpecialization = s; }

    @Override
    public String toString() {
        return String.format("Appointment[ID=%d, Patient=%s, Doctor=Dr.%s, Date=%s, Time=%s, Status=%s]",
                appointmentId, patientName, doctorName, appointmentDate, appointmentTime, status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Appointment)) return false;
        Appointment a = (Appointment) o;
        return appointmentId == a.appointmentId;
    }

    @Override
    public int hashCode() { return Objects.hash(appointmentId); }
}
