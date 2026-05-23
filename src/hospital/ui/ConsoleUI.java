package hospital.ui;

import hospital.model.Appointment;
import hospital.model.Doctor;
import hospital.model.Patient;

import java.util.List;


public class ConsoleUI {

    
    public static final String RESET  = "\u001B[0m";
    public static final String GREEN  = "\u001B[32m";
    public static final String CYAN   = "\u001B[36m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED    = "\u001B[31m";
    public static final String BLUE   = "\u001B[34m";
    public static final String BOLD   = "\u001B[1m";

    public static void printHeader(String title) {
        String border = "═".repeat(60);
        System.out.println(CYAN + BOLD);
        System.out.println("╔" + border + "╗");
        System.out.printf("║  %-58s║%n", title);
        System.out.println("╚" + border + "╝" + RESET);
    }

    public static void printSuccess(String msg) {
        System.out.println(GREEN + "✔  " + msg + RESET);
    }

    public static void printError(String msg) {
        System.out.println(RED + "✘  " + msg + RESET);
    }

    public static void printInfo(String msg) {
        System.out.println(BLUE + "ℹ  " + msg + RESET);
    }

    public static void printSeparator() {
        System.out.println(YELLOW + "─".repeat(62) + RESET);
    }

    // ---------------------------------------------------------------- PATIENTS
    public static void printPatientTable(List<Patient> patients) {
        if (patients.isEmpty()) { printInfo("No patients found."); return; }
        System.out.printf(BOLD + "%-5s %-20s %-5s %-8s %-6s %-15s %-20s%n" + RESET,
                "ID", "Name", "Age", "Gender", "Blood", "Phone", "Email");
        printSeparator();
        for (Patient p : patients) {
            System.out.printf("%-5d %-20s %-5d %-8s %-6s %-15s %-20s%n",
                    p.getPatientId(), p.getName(), p.getAge(),
                    p.getGender(), nvl(p.getBloodGroup()), nvl(p.getPhone()), nvl(p.getEmail()));
        }
    }

    public static void printPatientDetail(Patient p) {
        printSeparator();
        System.out.printf(BOLD + "Patient Details — ID: %d%n" + RESET, p.getPatientId());
        printSeparator();
        field("Name",          p.getName());
        field("Age",           String.valueOf(p.getAge()));
        field("Gender",        p.getGender());
        field("Blood Group",   nvl(p.getBloodGroup()));
        field("Phone",         nvl(p.getPhone()));
        field("Email",         nvl(p.getEmail()));
        field("Address",       nvl(p.getAddress()));
        field("Registered On", String.valueOf(p.getRegistrationDate()));
        field("Medical Hx",   nvl(p.getMedicalHistory()));
        printSeparator();
    }

    // ----------------------------------------------------------------- DOCTORS
    public static void printDoctorTable(List<Doctor> doctors) {
        if (doctors.isEmpty()) { printInfo("No doctors found."); return; }
        System.out.printf(BOLD + "%-5s %-22s %-18s %-6s %-15s %-10s%n" + RESET,
                "ID", "Name", "Specialization", "Exp", "Phone", "Fee (₹)");
        printSeparator();
        for (Doctor d : doctors) {
            System.out.printf("%-5d Dr.%-19s %-18s %-6d %-15s %-10.2f%n",
                    d.getDoctorId(), d.getName(), d.getSpecialization(),
                    d.getExperienceYears(), nvl(d.getPhone()), d.getConsultationFee());
        }
    }

    public static void printDoctorDetail(Doctor d) {
        printSeparator();
        System.out.printf(BOLD + "Doctor Details — ID: %d%n" + RESET, d.getDoctorId());
        printSeparator();
        field("Name",              "Dr. " + d.getName());
        field("Specialization",    d.getSpecialization());
        field("Qualification",     nvl(d.getQualification()));
        field("Experience",        d.getExperienceYears() + " years");
        field("Phone",             nvl(d.getPhone()));
        field("Email",             nvl(d.getEmail()));
        field("Available Days",    nvl(d.getAvailableDays()));
        field("Consultation Time", nvl(d.getConsultationTime()));
        field("Consultation Fee",  String.format("₹%.2f", d.getConsultationFee()));
        printSeparator();
    }

    // ----------------------------------------------------------- APPOINTMENTS
    public static void printAppointmentTable(List<Appointment> appointments) {
        if (appointments.isEmpty()) { printInfo("No appointments found."); return; }
        System.out.printf(BOLD + "%-5s %-18s %-20s %-12s %-8s %-12s %-10s%n" + RESET,
                "ID", "Patient", "Doctor", "Date", "Time", "Status", "Fee (₹)");
        printSeparator();
        for (Appointment a : appointments) {
            System.out.printf("%-5d %-18s Dr.%-17s %-12s %-8s %-12s %-10.2f%n",
                    a.getAppointmentId(),
                    nvl(a.getPatientName()), nvl(a.getDoctorName()),
                    a.getAppointmentDate(), a.getAppointmentTime(),
                    a.getStatus(), a.getFeeCharged());
        }
    }

    public static void printAppointmentDetail(Appointment a) {
        printSeparator();
        System.out.printf(BOLD + "Appointment Details — ID: %d%n" + RESET, a.getAppointmentId());
        printSeparator();
        field("Patient",    nvl(a.getPatientName()) + " (ID: " + a.getPatientId() + ")");
        field("Doctor",     "Dr. " + nvl(a.getDoctorName()) + " (ID: " + a.getDoctorId() + ")");
        field("Spec.",      nvl(a.getDoctorSpecialization()));
        field("Date",       String.valueOf(a.getAppointmentDate()));
        field("Time",       String.valueOf(a.getAppointmentTime()));
        field("Reason",     nvl(a.getReason()));
        field("Status",     a.getStatus().name());
        field("Notes",      nvl(a.getNotes()));
        field("Fee",        String.format("₹%.2f", a.getFeeCharged()));
        printSeparator();
    }

    // ---------------------------------------------------------------- HELPERS
    private static void field(String label, String value) {
        System.out.printf("  %-20s : %s%n", label, value);
    }

    private static String nvl(String s) { return s != null ? s : "-"; }
}
