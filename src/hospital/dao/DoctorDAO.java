package hospital.dao;

import hospital.exception.HospitalException;
import hospital.model.Doctor;
import hospital.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DoctorDAO implements GenericDAO<Doctor, Integer> {

    private Connection conn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Doctor save(Doctor d) throws HospitalException {
        String sql = """
            INSERT INTO doctors
              (name, specialization, qualification, phone, email,
               experience_years, available_days, consultation_time, consultation_fee)
            VALUES (?,?,?,?,?,?,?,?,?)
        """;
        try (PreparedStatement ps = conn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, d.getName());
            ps.setString(2, d.getSpecialization());
            ps.setString(3, d.getQualification());
            ps.setString(4, d.getPhone());
            ps.setString(5, d.getEmail());
            ps.setInt   (6, d.getExperienceYears());
            ps.setString(7, d.getAvailableDays());
            ps.setString(8, d.getConsultationTime());
            ps.setDouble(9, d.getConsultationFee());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) d.setDoctorId(rs.getInt(1));
            }
            return d;
        } catch (SQLException e) {
            throw new HospitalException("Error saving doctor: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Doctor> findById(Integer id) throws HospitalException {
        String sql = "SELECT * FROM doctors WHERE doctor_id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new HospitalException("Error finding doctor: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Doctor> findAll() throws HospitalException {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT * FROM doctors ORDER BY doctor_id";
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new HospitalException("Error fetching doctors: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public boolean update(Doctor d) throws HospitalException {
        String sql = """
            UPDATE doctors SET
              name=?, specialization=?, qualification=?, phone=?, email=?,
              experience_years=?, available_days=?, consultation_time=?, consultation_fee=?
            WHERE doctor_id=?
        """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, d.getName());
            ps.setString(2, d.getSpecialization());
            ps.setString(3, d.getQualification());
            ps.setString(4, d.getPhone());
            ps.setString(5, d.getEmail());
            ps.setInt   (6, d.getExperienceYears());
            ps.setString(7, d.getAvailableDays());
            ps.setString(8, d.getConsultationTime());
            ps.setDouble(9, d.getConsultationFee());
            ps.setInt  (10, d.getDoctorId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new HospitalException("Error updating doctor: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(Integer id) throws HospitalException {
        String sql = "DELETE FROM doctors WHERE doctor_id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new HospitalException("Error deleting doctor: " + e.getMessage(), e);
        }
    }

    public List<Doctor> findBySpecialization(String spec) throws HospitalException {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT * FROM doctors WHERE LOWER(specialization) LIKE ? ORDER BY name";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, "%" + spec.toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new HospitalException("Error searching doctors: " + e.getMessage(), e);
        }
        return list;
    }

    private Doctor map(ResultSet rs) throws SQLException {
        Doctor d = new Doctor();
        d.setDoctorId        (rs.getInt   ("doctor_id"));
        d.setName            (rs.getString("name"));
        d.setSpecialization  (rs.getString("specialization"));
        d.setQualification   (rs.getString("qualification"));
        d.setPhone           (rs.getString("phone"));
        d.setEmail           (rs.getString("email"));
        d.setExperienceYears (rs.getInt   ("experience_years"));
        d.setAvailableDays   (rs.getString("available_days"));
        d.setConsultationTime(rs.getString("consultation_time"));
        d.setConsultationFee (rs.getDouble("consultation_fee"));
        return d;
    }
}
