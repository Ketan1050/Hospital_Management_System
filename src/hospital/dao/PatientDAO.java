package hospital.dao;

import hospital.exception.HospitalException;
import hospital.exception.RecordNotFoundException;
import hospital.model.Patient;
import hospital.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientDAO implements GenericDAO<Patient, Integer> {

    private Connection conn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    // ------------------------------------------------------------------ SAVE
    @Override
    public Patient save(Patient p) throws HospitalException {
        String sql = """
            INSERT INTO patients
              (name, age, gender, blood_group, phone, email, address, registration_date, medical_history)
            VALUES (?,?,?,?,?,?,?,?,?)
        """;
        try (PreparedStatement ps = conn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getName());
            ps.setInt   (2, p.getAge());
            ps.setString(3, p.getGender());
            ps.setString(4, p.getBloodGroup());
            ps.setString(5, p.getPhone());
            ps.setString(6, p.getEmail());
            ps.setString(7, p.getAddress());
            ps.setDate  (8, Date.valueOf(p.getRegistrationDate()));
            ps.setString(9, p.getMedicalHistory());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) p.setPatientId(rs.getInt(1));
            }
            return p;
        } catch (SQLException e) {
            throw new HospitalException("Error saving patient: " + e.getMessage(), e);
        }
    }

    // --------------------------------------------------------------- FIND BY ID
    @Override
    public Optional<Patient> findById(Integer id) throws HospitalException {
        String sql = "SELECT * FROM patients WHERE patient_id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new HospitalException("Error finding patient by ID: " + e.getMessage(), e);
        }
    }

    // --------------------------------------------------------------- FIND ALL
    @Override
    public List<Patient> findAll() throws HospitalException {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY patient_id";
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new HospitalException("Error fetching patients: " + e.getMessage(), e);
        }
        return list;
    }

    // ----------------------------------------------------------------- UPDATE
    @Override
    public boolean update(Patient p) throws HospitalException {
        String sql = """
            UPDATE patients SET
              name=?, age=?, gender=?, blood_group=?, phone=?,
              email=?, address=?, medical_history=?
            WHERE patient_id=?
        """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setInt   (2, p.getAge());
            ps.setString(3, p.getGender());
            ps.setString(4, p.getBloodGroup());
            ps.setString(5, p.getPhone());
            ps.setString(6, p.getEmail());
            ps.setString(7, p.getAddress());
            ps.setString(8, p.getMedicalHistory());
            ps.setInt   (9, p.getPatientId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new HospitalException("Error updating patient: " + e.getMessage(), e);
        }
    }

    // ----------------------------------------------------------------- DELETE
    @Override
    public boolean delete(Integer id) throws HospitalException {
        String sql = "DELETE FROM patients WHERE patient_id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new HospitalException("Error deleting patient: " + e.getMessage(), e);
        }
    }

    // ---------------------------------------------------------- SEARCH BY NAME
    public List<Patient> searchByName(String name) throws HospitalException {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT * FROM patients WHERE LOWER(name) LIKE ? ORDER BY name";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, "%" + name.toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new HospitalException("Error searching patients: " + e.getMessage(), e);
        }
        return list;
    }

    // --------------------------------------------------------------- MAPPER
    private Patient map(ResultSet rs) throws SQLException {
        Patient p = new Patient();
        p.setPatientId       (rs.getInt   ("patient_id"));
        p.setName            (rs.getString("name"));
        p.setAge             (rs.getInt   ("age"));
        p.setGender          (rs.getString("gender"));
        p.setBloodGroup      (rs.getString("blood_group"));
        p.setPhone           (rs.getString("phone"));
        p.setEmail           (rs.getString("email"));
        p.setAddress         (rs.getString("address"));
        p.setRegistrationDate(rs.getDate  ("registration_date").toLocalDate());
        p.setMedicalHistory  (rs.getString("medical_history"));
        return p;
    }
}
