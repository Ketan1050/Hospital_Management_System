package hospital.ui;

import hospital.exception.HospitalException;
import hospital.model.Appointment;
import hospital.model.Doctor;
import hospital.model.Patient;
import hospital.service.AppointmentService;
import hospital.service.DoctorService;
import hospital.service.PatientService;
import hospital.util.DatabaseConnection;
import hospital.util.SchemaInitializer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class HospitalApp {

    private static final Scanner sc = new Scanner(System.in);
    private static final PatientService     patientSvc     = new PatientService();
    private static final DoctorService      doctorSvc      = new DoctorService();
    private static final AppointmentService appointmentSvc = new AppointmentService();

    public static void main(String[] args) {
        // Init DB schema
        SchemaInitializer.initialize();

       
        System.out.println("  Hospital Management System" + ConsoleUI.RESET);
        System.out.println();

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1  -> patientMenu();
                case 2  -> doctorMenu();
                case 3  -> appointmentMenu();
                case 4  -> reportsMenu();
                case 0  -> running = false;
                default -> ConsoleUI.printError("Invalid option. Try again.");
            }
        }

        DatabaseConnection.getInstance().closeConnection();
        System.out.println(ConsoleUI.GREEN + "\nThank you for using HMS. Goodbye!" + ConsoleUI.RESET);
    }

    // ================================================================ MENUS
    private static void printMainMenu() {
        ConsoleUI.printHeader("MAIN MENU");
        System.out.println("  1. Patient Management");
        System.out.println("  2. Doctor Management");
        System.out.println("  3. Appointment Management");
        System.out.println("  4. Reports & Analytics");
        System.out.println("  0. Exit");
        ConsoleUI.printSeparator();
    }

    // ---------------------------------------------------------------- PATIENT
    private static void patientMenu() {
        boolean back = false;
        while (!back) {
            ConsoleUI.printHeader("PATIENT MANAGEMENT");
            System.out.println("  1. Register New Patient");
            System.out.println("  2. View All Patients");
            System.out.println("  3. Search Patient by Name");
            System.out.println("  4. View Patient Details");
            System.out.println("  5. Update Patient");
            System.out.println("  6. Delete Patient");
            System.out.println("  0. Back");
            ConsoleUI.printSeparator();
            int ch = readInt("Enter choice: ");
            switch (ch) {
                case 1 -> addPatient();
                case 2 -> listAllPatients();
                case 3 -> searchPatient();
                case 4 -> viewPatient();
                case 5 -> updatePatient();
                case 6 -> deletePatient();
                case 0 -> back = true;
                default -> ConsoleUI.printError("Invalid option.");
            }
        }
    }

    private static void addPatient() {
        ConsoleUI.printHeader("REGISTER NEW PATIENT");
        try {
            Patient p = new Patient(
                readString("Name: "),
                readInt("Age: "),
                readString("Gender (Male/Female/Other): "),
                readString("Blood Group (e.g. A+): "),
                readString("Phone: "),
                readString("Email: "),
                readString("Address: "),
                readString("Medical History (or press Enter): ")
            );
            Patient saved = patientSvc.addPatient(p);
            ConsoleUI.printSuccess("Patient registered successfully! ID: " + saved.getPatientId());
        } catch (HospitalException e) {
            ConsoleUI.printError(e.getMessage());
        }
    }

    private static void listAllPatients() {
        ConsoleUI.printHeader("ALL PATIENTS");
        try {
            ConsoleUI.printPatientTable(patientSvc.getAllPatients());
        } catch (HospitalException e) {
            ConsoleUI.printError(e.getMessage());
        }
    }

    private static void searchPatient() {
        String name = readString("Enter name to search: ");
        try {
            List<Patient> results = patientSvc.searchByName(name);
            ConsoleUI.printPatientTable(results);
        } catch (HospitalException e) {
            ConsoleUI.printError(e.getMessage());
        }
    }

    private static void viewPatient() {
        int id = readInt("Enter Patient ID: ");
        try {
            ConsoleUI.printPatientDetail(patientSvc.getPatientById(id));
        } catch (HospitalException e) {
            ConsoleUI.printError(e.getMessage());
        }
    }

    private static void updatePatient() {
        int id = readInt("Enter Patient ID to update: ");
        try {
            Patient p = patientSvc.getPatientById(id);
            System.out.println("Leave blank to keep current value.");
            String name = readString("Name [" + p.getName() + "]: ");
            if (!name.isBlank()) p.setName(name);
            String ageStr = readString("Age [" + p.getAge() + "]: ");
            if (!ageStr.isBlank()) p.setAge(Integer.parseInt(ageStr));
            String phone = readString("Phone [" + p.getPhone() + "]: ");
            if (!phone.isBlank()) p.setPhone(phone);
            String email = readString("Email [" + p.getEmail() + "]: ");
            if (!email.isBlank()) p.setEmail(email);
            String addr = readString("Address [" + p.getAddress() + "]: ");
            if (!addr.isBlank()) p.setAddress(addr);
            String hist = readString("Medical History [" + p.getMedicalHistory() + "]: ");
            if (!hist.isBlank()) p.setMedicalHistory(hist);

            if (patientSvc.updatePatient(p))
                ConsoleUI.printSuccess("Patient updated successfully.");
            else
                ConsoleUI.printError("Update failed — patient not found.");
        } catch (HospitalException e) {
            ConsoleUI.printError(e.getMessage());
        }
    }

    private static void deletePatient() {
        int id = readInt("Enter Patient ID to delete: ");
        String confirm = readString("Are you sure? (yes/no): ");
        if (!confirm.equalsIgnoreCase("yes")) { ConsoleUI.printInfo("Cancelled."); return; }
        try {
            if (patientSvc.deletePatient(id))
                ConsoleUI.printSuccess("Patient deleted.");
            else
                ConsoleUI.printError("Patient not found.");
        } catch (HospitalException e) {
            ConsoleUI.printError(e.getMessage());
        }
    }

    // ----------------------------------------------------------------- DOCTOR
    private static void doctorMenu() {
        boolean back = false;
        while (!back) {
            ConsoleUI.printHeader("DOCTOR MANAGEMENT");
            System.out.println("  1. Add New Doctor");
            System.out.println("  2. View All Doctors");
            System.out.println("  3. Search by Specialization");
            System.out.println("  4. View Doctor Details");
            System.out.println("  5. Update Doctor");
            System.out.println("  6. Delete Doctor");
            System.out.println("  0. Back");
            ConsoleUI.printSeparator();
            int ch = readInt("Enter choice: ");
            switch (ch) {
                case 1 -> addDoctor();
                case 2 -> listAllDoctors();
                case 3 -> searchDoctor();
                case 4 -> viewDoctor();
                case 5 -> updateDoctor();
                case 6 -> deleteDoctor();
                case 0 -> back = true;
                default -> ConsoleUI.printError("Invalid option.");
            }
        }
    }

    private static void addDoctor() {
        ConsoleUI.printHeader("ADD NEW DOCTOR");
        try {
            Doctor d = new Doctor(
                readString("Name: "),
                readString("Specialization: "),
                readString("Qualification: "),
                readString("Phone: "),
                readString("Email: "),
                readInt("Years of Experience: "),
                readString("Available Days (e.g. Mon,Tue,Wed): "),
                readString("Consultation Time (e.g. 09:00-13:00): "),
                Double.parseDouble(readString("Consultation Fee (₹): "))
            );
            Doctor saved = doctorSvc.addDoctor(d);
            ConsoleUI.printSuccess("Doctor added successfully! ID: " + saved.getDoctorId());
        } catch (HospitalException | NumberFormatException e) {
            ConsoleUI.printError(e.getMessage());
        }
    }

    private static void listAllDoctors() {
        ConsoleUI.printHeader("ALL DOCTORS");
        try {
            ConsoleUI.printDoctorTable(doctorSvc.getAllDoctors());
        } catch (HospitalException e) {
            ConsoleUI.printError(e.getMessage());
        }
    }

    private static void searchDoctor() {
        String spec = readString("Enter specialization to search: ");
        try {
            ConsoleUI.printDoctorTable(doctorSvc.getDoctorsBySpecialization(spec));
        } catch (HospitalException e) {
            ConsoleUI.printError(e.getMessage());
        }
    }

    private static void viewDoctor() {
        int id = readInt("Enter Doctor ID: ");
        try {
            ConsoleUI.printDoctorDetail(doctorSvc.getDoctorById(id));
        } catch (HospitalException e) {
            ConsoleUI.printError(e.getMessage());
        }
    }

    private static void updateDoctor() {
        int id = readInt("Enter Doctor ID to update: ");
        try {
            Doctor d = doctorSvc.getDoctorById(id);
            System.out.println("Leave blank to keep current value.");
            String name = readString("Name [" + d.getName() + "]: ");
            if (!name.isBlank()) d.setName(name);
            String spec = readString("Specialization [" + d.getSpecialization() + "]: ");
            if (!spec.isBlank()) d.setSpecialization(spec);
            String phone = readString("Phone [" + d.getPhone() + "]: ");
            if (!phone.isBlank()) d.setPhone(phone);
            String feeStr = readString("Consultation Fee [" + d.getConsultationFee() + "]: ");
            if (!feeStr.isBlank()) d.setConsultationFee(Double.parseDouble(feeStr));

            if (doctorSvc.updateDoctor(d))
                ConsoleUI.printSuccess("Doctor updated successfully.");
            else
                ConsoleUI.printError("Doctor not found.");
        } catch (HospitalException | NumberFormatException e) {
            ConsoleUI.printError(e.getMessage());
        }
    }

    private static void deleteDoctor() {
        int id = readInt("Enter Doctor ID to delete: ");
        String confirm = readString("Are you sure? (yes/no): ");
        if (!confirm.equalsIgnoreCase("yes")) { ConsoleUI.printInfo("Cancelled."); return; }
        try {
            if (doctorSvc.deleteDoctor(id))
                ConsoleUI.printSuccess("Doctor deleted.");
            else
                ConsoleUI.printError("Doctor not found.");
        } catch (HospitalException e) {
            ConsoleUI.printError(e.getMessage());
        }
    }

    // ------------------------------------------------------------- APPOINTMENT
    private static void appointmentMenu() {
        boolean back = false;
        while (!back) {
            ConsoleUI.printHeader("APPOINTMENT MANAGEMENT");
            System.out.println("  1. Book Appointment");
            System.out.println("  2. View All Appointments");
            System.out.println("  3. Today's Appointments");
            System.out.println("  4. Appointments by Date");
            System.out.println("  5. Appointments by Patient");
            System.out.println("  6. Appointments by Doctor");
            System.out.println("  7. View Appointment Details");
            System.out.println("  8. Complete Appointment");
            System.out.println("  9. Cancel Appointment");
            System.out.println("  0. Back");
            ConsoleUI.printSeparator();
            int ch = readInt("Enter choice: ");
            switch (ch) {
                case 1 -> bookAppointment();
                case 2 -> listAllAppointments();
                case 3 -> todaysAppointments();
                case 4 -> appointmentsByDate();
                case 5 -> appointmentsByPatient();
                case 6 -> appointmentsByDoctor();
                case 7 -> viewAppointment();
                case 8 -> completeAppointment();
                case 9 -> cancelAppointment();
                case 0 -> back = true;
                default -> ConsoleUI.printError("Invalid option.");
            }
        }
    }

    private static void bookAppointment() {
        ConsoleUI.printHeader("BOOK APPOINTMENT");
        try {
            int pid = readInt("Patient ID: ");
            int did = readInt("Doctor ID: ");

            // Show doctor fee
            Doctor doc = doctorSvc.getDoctorById(did);

            LocalDate date = readDate("Appointment Date (YYYY-MM-DD): ");
            LocalTime time = readTime("Appointment Time (HH:MM): ");
            String reason = readString("Reason for visit: ");

            Appointment a = new Appointment(pid, did, date, time, reason, doc.getConsultationFee());
            Appointment saved = appointmentSvc.bookAppointment(a);
            ConsoleUI.printSuccess("Appointment booked! ID: " + saved.getAppointmentId()
                    + "  Fee: ₹" + doc.getConsultationFee());
        } catch (HospitalException | DateTimeParseException e) {
            ConsoleUI.printError(e.getMessage());
        }
    }

    private static void listAllAppointments() {
        ConsoleUI.printHeader("ALL APPOINTMENTS");
        try {
            ConsoleUI.printAppointmentTable(appointmentSvc.getAllAppointments());
        } catch (HospitalException e) {
            ConsoleUI.printError(e.getMessage());
        }
    }

    private static void todaysAppointments() {
        ConsoleUI.printHeader("TODAY'S APPOINTMENTS — " + LocalDate.now());
        try {
            ConsoleUI.printAppointmentTable(appointmentSvc.getTodaysAppointments());
        } catch (HospitalException e) {
            ConsoleUI.printError(e.getMessage());
        }
    }

    private static void appointmentsByDate() {
        LocalDate date = readDate("Enter Date (YYYY-MM-DD): ");
        try {
            ConsoleUI.printAppointmentTable(appointmentSvc.getAppointmentsByDate(date));
        } catch (HospitalException e) {
            ConsoleUI.printError(e.getMessage());
        }
    }

    private static void appointmentsByPatient() {
        int id = readInt("Enter Patient ID: ");
        try {
            ConsoleUI.printAppointmentTable(appointmentSvc.getAppointmentsByPatient(id));
        } catch (HospitalException e) {
            ConsoleUI.printError(e.getMessage());
        }
    }

    private static void appointmentsByDoctor() {
        int id = readInt("Enter Doctor ID: ");
        try {
            ConsoleUI.printAppointmentTable(appointmentSvc.getAppointmentsByDoctor(id));
        } catch (HospitalException e) {
            ConsoleUI.printError(e.getMessage());
        }
    }

    private static void viewAppointment() {
        int id = readInt("Enter Appointment ID: ");
        try {
            ConsoleUI.printAppointmentDetail(appointmentSvc.getAppointmentById(id));
        } catch (HospitalException e) {
            ConsoleUI.printError(e.getMessage());
        }
    }

    private static void completeAppointment() {
        int id = readInt("Enter Appointment ID to mark complete: ");
        try {
            if (appointmentSvc.completeAppointment(id))
                ConsoleUI.printSuccess("Appointment marked as COMPLETED.");
            else
                ConsoleUI.printError("Appointment not found.");
        } catch (HospitalException e) {
            ConsoleUI.printError(e.getMessage());
        }
    }

    private static void cancelAppointment() {
        int id = readInt("Enter Appointment ID to cancel: ");
        try {
            if (appointmentSvc.cancelAppointment(id))
                ConsoleUI.printSuccess("Appointment CANCELLED.");
            else
                ConsoleUI.printError("Appointment not found.");
        } catch (HospitalException e) {
            ConsoleUI.printError(e.getMessage());
        }
    }

    // ---------------------------------------------------------------- REPORTS
    private static void reportsMenu() {
        boolean back = false;
        while (!back) {
            ConsoleUI.printHeader("REPORTS & ANALYTICS");
            System.out.println("  1. Patients Grouped by Blood Group");
            System.out.println("  2. Patients Sorted by Age");
            System.out.println("  3. Doctors Grouped by Specialization");
            System.out.println("  4. Senior Doctors (10+ Years Experience)");
            System.out.println("  5. Appointments by Status");
            System.out.println("  6. Total Revenue from Completed Appointments");
            System.out.println("  0. Back");
            ConsoleUI.printSeparator();
            int ch = readInt("Enter choice: ");
            switch (ch) {
                case 1 -> patientsByBloodGroup();
                case 2 -> patientsByAge();
                case 3 -> doctorsBySpecialization();
                case 4 -> seniorDoctors();
                case 5 -> appointmentsByStatus();
                case 6 -> totalRevenue();
                case 0 -> back = true;
                default -> ConsoleUI.printError("Invalid option.");
            }
        }
    }

    private static void patientsByBloodGroup() {
        ConsoleUI.printHeader("PATIENTS BY BLOOD GROUP");
        try {
            Map<String, List<Patient>> grouped = patientSvc.groupByBloodGroup();
            grouped.forEach((bg, list) -> {
                System.out.println(ConsoleUI.YELLOW + "\n  Blood Group: " + bg
                        + " (" + list.size() + " patients)" + ConsoleUI.RESET);
                ConsoleUI.printPatientTable(list);
            });
        } catch (HospitalException e) {
            ConsoleUI.printError(e.getMessage());
        }
    }

    private static void patientsByAge() {
        ConsoleUI.printHeader("PATIENTS SORTED BY AGE");
        try {
            ConsoleUI.printPatientTable(patientSvc.getPatientsSortedByAge());
        } catch (HospitalException e) {
            ConsoleUI.printError(e.getMessage());
        }
    }

    private static void doctorsBySpecialization() {
        ConsoleUI.printHeader("DOCTORS BY SPECIALIZATION");
        try {
            Map<String, List<Doctor>> grouped = doctorSvc.groupBySpecialization();
            grouped.forEach((spec, list) -> {
                System.out.println(ConsoleUI.YELLOW + "\n  " + spec
                        + " (" + list.size() + " doctors)" + ConsoleUI.RESET);
                ConsoleUI.printDoctorTable(list);
            });
        } catch (HospitalException e) {
            ConsoleUI.printError(e.getMessage());
        }
    }

    private static void seniorDoctors() {
        ConsoleUI.printHeader("SENIOR DOCTORS (10+ Years Experience)");
        try {
            ConsoleUI.printDoctorTable(doctorSvc.getSeniorDoctors());
        } catch (HospitalException e) {
            ConsoleUI.printError(e.getMessage());
        }
    }

    private static void appointmentsByStatus() {
        ConsoleUI.printHeader("APPOINTMENTS BY STATUS");
        try {
            Map<Appointment.Status, List<Appointment>> grouped = appointmentSvc.groupByStatus();
            grouped.forEach((status, list) -> {
                System.out.println(ConsoleUI.YELLOW + "\n  " + status
                        + " (" + list.size() + ")" + ConsoleUI.RESET);
                ConsoleUI.printAppointmentTable(list);
            });
        } catch (HospitalException e) {
            ConsoleUI.printError(e.getMessage());
        }
    }

    private static void totalRevenue() {
        ConsoleUI.printHeader("TOTAL REVENUE");
        try {
            double total = appointmentSvc.getTotalRevenue();
            ConsoleUI.printInfo(String.format("Total revenue from completed appointments: ₹%.2f", total));
        } catch (HospitalException e) {
            ConsoleUI.printError(e.getMessage());
        }
    }

    // ---------------------------------------------------------------- HELPERS
    private static String readString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String line = sc.nextLine().trim();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                ConsoleUI.printError("Please enter a valid integer.");
            }
        }
    }

    private static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return LocalDate.parse(sc.nextLine().trim());
            } catch (DateTimeParseException e) {
                ConsoleUI.printError("Invalid date format. Use YYYY-MM-DD.");
            }
        }
    }

    private static LocalTime readTime(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return LocalTime.parse(sc.nextLine().trim());
            } catch (DateTimeParseException e) {
                ConsoleUI.printError("Invalid time format. Use HH:MM.");
            }
        }
    }
}
