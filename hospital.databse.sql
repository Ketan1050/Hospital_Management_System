CREATE DATABASE IF NOT EXISTS hospital_db;
    

USE hospital_db;


DROP TABLE IF EXISTS appointments;
DROP TABLE IF EXISTS patients;
DROP TABLE IF EXISTS doctors;


CREATE TABLE patients (
    patient_id        INT AUTO_INCREMENT PRIMARY KEY,
    name              VARCHAR(100) NOT NULL,
    age               INT NOT NULL CHECK (age > 0 AND age < 150),
    gender            VARCHAR(10) NOT NULL,
    blood_group       VARCHAR(5),
    phone             VARCHAR(15),
    email             VARCHAR(100),
    address           VARCHAR(255),
    registration_date DATE NOT NULL,
    medical_history   TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE doctors (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE appointments (
    appointment_id    INT AUTO_INCREMENT PRIMARY KEY,
    patient_id        INT NOT NULL,
    doctor_id         INT NOT NULL,
    appointment_date  DATE NOT NULL,
    appointment_time  TIME NOT NULL,
    reason            VARCHAR(255),
    status            VARCHAR(20) DEFAULT 'SCHEDULED',
    notes             TEXT,
    fee_charged       DECIMAL(10,2) DEFAULT 0.00,
    CONSTRAINT fk_apt_patient FOREIGN KEY (patient_id)
        REFERENCES patients(patient_id) ON DELETE CASCADE,
    CONSTRAINT fk_apt_doctor  FOREIGN KEY (doctor_id)
        REFERENCES doctors(doctor_id)  ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


INSERT INTO doctors (name, specialization, qualification, phone, email,
                     experience_years, available_days, consultation_time, consultation_fee)
VALUES
('Rajesh Sharma',   'Cardiology',       'MD, DM Cardiology', '9876543210', 'rajesh@hospital.com',  15, 'Mon,Wed,Fri', '09:00-13:00', 800.00),
('Priya Mehta',     'Neurology',        'MD, DM Neurology',  '9876543211', 'priya@hospital.com',   12, 'Tue,Thu,Sat', '10:00-14:00', 700.00),
('Anil Kumar',      'Orthopedics',      'MS Orthopedics',    '9876543212', 'anil@hospital.com',    10, 'Mon,Tue,Wed', '08:00-12:00', 600.00),
('Sunita Patel',    'Pediatrics',       'MD Pediatrics',     '9876543213', 'sunita@hospital.com',   8, 'Wed,Thu,Fri', '11:00-15:00', 500.00),
('Vikram Singh',    'General Medicine', 'MBBS, MD',          '9876543214', 'vikram@hospital.com',   5, 'Mon,Tue,Thu', '09:00-17:00', 400.00);


INSERT INTO patients (name, age, gender, blood_group, phone, email, address, registration_date, medical_history)
VALUES
('Amit Joshi',    35, 'Male',   'A+', '9000000001', 'amit@gmail.com',   'Pune',    CURDATE(), 'Hypertension'),
('Sneha Kulkarni',28, 'Female', 'B+', '9000000002', 'sneha@gmail.com',  'Mumbai',  CURDATE(), 'None'),
('Ravi Desai',    50, 'Male',   'O+', '9000000003', 'ravi@gmail.com',   'Nashik',  CURDATE(), 'Diabetes Type 2'),
('Pooja Nair',    22, 'Female', 'AB-','9000000004', 'pooja@gmail.com',  'Nagpur',  CURDATE(), 'Asthma'),
('Suresh Rao',    60, 'Male',   'A-', '9000000005', 'suresh@gmail.com', 'Kolhapur',CURDATE(), 'Arthritis');

INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, reason, status, fee_charged)
VALUES
(1, 1, CURDATE(),                '10:00:00', 'Chest pain checkup',   'SCHEDULED', 800.00),
(2, 2, CURDATE(),                '11:00:00', 'Migraine consultation', 'SCHEDULED', 700.00),
(3, 5, DATE_SUB(CURDATE(),INTERVAL 1 DAY), '09:00:00', 'Fever and cough', 'COMPLETED', 400.00),
(4, 4, DATE_SUB(CURDATE(),INTERVAL 2 DAY), '12:00:00', 'Child vaccination','COMPLETED', 500.00),
(5, 3, DATE_ADD(CURDATE(),INTERVAL 1 DAY), '08:30:00', 'Knee pain',       'SCHEDULED', 600.00);

