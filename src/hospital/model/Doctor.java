package hospital.model;

import java.util.Objects;

public class Doctor {
    private int doctorId;
    private String name;
    private String specialization;
    private String qualification;
    private String phone;
    private String email;
    private int experienceYears;
    private String availableDays;  
    private String consultationTime; 
    private double consultationFee;

    public Doctor() {}

    public Doctor(String name, String specialization, String qualification,
                  String phone, String email, int experienceYears,
                  String availableDays, String consultationTime, double consultationFee) {
        this.name = name;
        this.specialization = specialization;
        this.qualification = qualification;
        this.phone = phone;
        this.email = email;
        this.experienceYears = experienceYears;
        this.availableDays = availableDays;
        this.consultationTime = consultationTime;
        this.consultationFee = consultationFee;
    }

    public int getDoctorId()                    { return doctorId; }
    public void setDoctorId(int id)             { this.doctorId = id; }
    public String getName()                     { return name; }
    public void setName(String name)            { this.name = name; }
    public String getSpecialization()           { return specialization; }
    public void setSpecialization(String s)     { this.specialization = s; }
    public String getQualification()            { return qualification; }
    public void setQualification(String q)      { this.qualification = q; }
    public String getPhone()                    { return phone; }
    public void setPhone(String phone)          { this.phone = phone; }
    public String getEmail()                    { return email; }
    public void setEmail(String email)          { this.email = email; }
    public int getExperienceYears()             { return experienceYears; }
    public void setExperienceYears(int y)       { this.experienceYears = y; }
    public String getAvailableDays()            { return availableDays; }
    public void setAvailableDays(String d)      { this.availableDays = d; }
    public String getConsultationTime()         { return consultationTime; }
    public void setConsultationTime(String t)   { this.consultationTime = t; }
    public double getConsultationFee()          { return consultationFee; }
    public void setConsultationFee(double fee)  { this.consultationFee = fee; }

    @Override
    public String toString() {
        return String.format("Doctor[ID=%d, Name=Dr.%s, Spec=%s, Exp=%d yrs, Fee=%.2f]",
                doctorId, name, specialization, experienceYears, consultationFee);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctor)) return false;
        Doctor d = (Doctor) o;
        return doctorId == d.doctorId;
    }

    @Override
    public int hashCode() { return Objects.hash(doctorId); }
}
