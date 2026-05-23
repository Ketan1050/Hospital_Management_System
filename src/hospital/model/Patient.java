package hospital.model;

import java.time.LocalDate;
import java.util.Objects;

public class Patient {
    private int patientId;
    private String name;
    private int age;
    private String gender;
    private String bloodGroup;
    private String phone;
    private String email;
    private String address;
    private LocalDate registrationDate;
    private String medicalHistory;

    public Patient() {}

    public Patient(String name, int age, String gender, String bloodGroup,
                   String phone, String email, String address, String medicalHistory) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.registrationDate = LocalDate.now();
        this.medicalHistory = medicalHistory;
    }

    public int getPatientId()               { return patientId; }
    public void setPatientId(int id)        { this.patientId = id; }
    public String getName()                 { return name; }
    public void setName(String name)        { this.name = name; }
    public int getAge()                     { return age; }
    public void setAge(int age)             { this.age = age; }
    public String getGender()               { return gender; }
    public void setGender(String gender)    { this.gender = gender; }
    public String getBloodGroup()           { return bloodGroup; }
    public void setBloodGroup(String bg)    { this.bloodGroup = bg; }
    public String getPhone()                { return phone; }
    public void setPhone(String phone)      { this.phone = phone; }
    public String getEmail()                { return email; }
    public void setEmail(String email)      { this.email = email; }
    public String getAddress()              { return address; }
    public void setAddress(String address)  { this.address = address; }
    public LocalDate getRegistrationDate()  { return registrationDate; }
    public void setRegistrationDate(LocalDate d) { this.registrationDate = d; }
    public String getMedicalHistory()       { return medicalHistory; }
    public void setMedicalHistory(String h) { this.medicalHistory = h; }

    @Override
    public String toString() {
        return String.format("Patient[ID=%d, Name=%s, Age=%d, Gender=%s, Blood=%s, Phone=%s]",
                patientId, name, age, gender, bloodGroup, phone);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient)) return false;
        Patient p = (Patient) o;
        return patientId == p.patientId;
    }

    @Override
    public int hashCode() { return Objects.hash(patientId); }
}
