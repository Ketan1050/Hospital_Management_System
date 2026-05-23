package hospital.util;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SchemaInitializer {

    private static final Logger LOG = Logger.getLogger(SchemaInitializer.class.getName());

    public static void initialize() {
        createDatabaseIfNotExists();
        createTables();
    }

    private static void createDatabaseIfNotExists() {
        
        String baseUrl = "jdbc:mysql://localhost:3306/?useSSL=false"
                       + "&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        try (Connection conn = DriverManager.getConnection(baseUrl, "root", "");
             Statement  stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS hospital_db "
                             + "CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
            LOG.info("Database 'hospital_db' ready.");

        } catch (SQLException e) {
           
        	LOG.log(Level.WARNING, "Could not auto-create database (may already exist): " + e.getMessage());
        }
    }

   
    private static void createTables() {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement  stmt = conn.createStatement()) {

            // -------- PATIENTS --------
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS patients (
                    patient_id        INT AUTO_INCREMENT PRIMARY KEY,
                    name              VARCHAR(100) NOT NULL,
                    age               INT NOT NULL,
                    gender            VARCHAR(10) NOT NULL,
                    blood_group       VARCHAR(5),
                    phone             VARCHAR(15),
                    email             VARCHAR(100),
                    address           VARCHAR(255),
                    registration_date DATE NOT NULL,
                    medical_history   TEXT
                ) ENGINE=InnoDB
            """);

            // -------- DOCTORS --------
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS doctors (
                    doctor_id          INT AUTO_INCREMENT PRIMARY KEY,
                    name               VARCHAR(100) NOT NULL,
                    specialization     VARCHAR(100) NOT NULL,
                    qualification      VARCHAR(100),
                    phone              VARCHAR(15),
                    email              VARCHAR(100),
                    experience_years   INT DEFAULT 0,
                    available_days     VARCHAR(50),
                    consultation_time  VARCHAR(30),
                    consultation_fee   DECIMAL(10,2) DEFAULT 0.00
                ) ENGINE=InnoDB
            """);

            // -------- APPOINTMENTS --------
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS appointments (
                    appointment_id    INT AUTO_INCREMENT PRIMARY KEY,
                    patient_id        INT NOT NULL,
                    doctor_id         INT NOT NULL,
                    appointment_date  DATE NOT NULL,
                    appointment_time  TIME NOT NULL,
                    reason            VARCHAR(255),
                    status            VARCHAR(20) DEFAULT 'SCHEDULED',
                    notes             TEXT,
                    fee_charged       DECIMAL(10,2) DEFAULT 0.00,
                    CONSTRAINT fk_patient FOREIGN KEY (patient_id)
                        REFERENCES patients(patient_id) ON DELETE CASCADE,
                    CONSTRAINT fk_doctor  FOREIGN KEY (doctor_id)
                        REFERENCES doctors(doctor_id)  ON DELETE CASCADE
                ) ENGINE=InnoDB
            """);

            LOG.info("All tables created / verified successfully.");

        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Table creation failed.", e);
            throw new RuntimeException("Schema initialisation failed: " + e.getMessage(), e);
        }
    }
}
