package hospital.dao;

import hospital.exception.HospitalException;
import hospital.model.Appointment;
import hospital.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppointmentDAO implements GenericDAO<Appointment, Integer> {

    private Connection conn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    private static final String JOIN_SQL = """
        SELECT a.*, p.name AS patient_name, d.name AS doctor_name, d.specialization
        FROM appointments a
        JOIN patients p ON a.patient_id = p.patient_id
        JOIN doctors  d ON a.doctor_id  = d.doctor_id
    """;

    @Override
    public Appointment save(Appointment a) throws HospitalException {
        String sql = """
            INSERT INTO appointments
              (patient_id, doctor_id, appointment_date, appointment_time,
               reason, status, notes, fee_charged)
            VALUES (?,?,?,?,?,?,?,?)
        """;
        try (PreparedStatement ps = conn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt   (1, a.getPatientId());
            ps.setInt   (2, a.getDoctorId());
            ps.setDate  (3, Date.valueOf(a.getAppointmentDate()));
            ps.setTime  (4, Time.valueOf(a.getAppointmentTime()));
            ps.setString(5, a.getReason());
            ps.setString(6, a.getStatus().name());
            ps.setString(7, a.getNotes());
            ps.setDouble(8, a.getFeeCharged());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) a.setAppointmentId(rs.getInt(1));
            }
            return a;
        } catch (SQLException e) {
            throw new HospitalException("Error saving appointment: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Appointment> findById(Integer id) throws HospitalException {
        String sql = JOIN_SQL + " WHERE a.appointment_id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new HospitalException("Error finding appointment: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Appointment> findAll() throws HospitalException {
        List<Appointment> list = new ArrayList<>();
        String sql = JOIN_SQL + " ORDER BY a.appointment_date, a.appointment_time";
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new HospitalException("Error fetching appointments: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public boolean update(Appointment a) throws HospitalException {
        String sql = """
            UPDATE appointments SET
              appointment_date=?, appointment_time=?, reason=?,
              status=?, notes=?, fee_charged=?
            WHERE appointment_id=?
        """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setDate  (1, Date.valueOf(a.getAppointmentDate()));
            ps.setTime  (2, Time.valueOf(a.getAppointmentTime()));
            ps.setString(3, a.getReason());
            ps.setString(4, a.getStatus().name());
            ps.setString(5, a.getNotes());
            ps.setDouble(6, a.getFeeCharged());
            ps.setInt   (7, a.getAppointmentId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new HospitalException("Error updating appointment: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(Integer id) throws HospitalException {
        String sql = "DELETE FROM appointments WHERE appointment_id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new HospitalException("Error deleting appointment: " + e.getMessage(), e);
        }
    }

    public List<Appointment> findByPatient(int patientId) throws HospitalException {
        List<Appointment> list = new ArrayList<>();
        String sql = JOIN_SQL + " WHERE a.patient_id = ? ORDER BY a.appointment_date DESC";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new HospitalException("Error fetching appointments by patient: " + e.getMessage(), e);
        }
        return list;
    }

    public List<Appointment> findByDoctor(int doctorId) throws HospitalException {
        List<Appointment> list = new ArrayList<>();
        String sql = JOIN_SQL + " WHERE a.doctor_id = ? ORDER BY a.appointment_date, a.appointment_time";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new HospitalException("Error fetching appointments by doctor: " + e.getMessage(), e);
        }
        return list;
    }

    public List<Appointment> findByDate(LocalDate date) throws HospitalException {
        List<Appointment> list = new ArrayList<>();
        String sql = JOIN_SQL + " WHERE a.appointment_date = ? ORDER BY a.appointment_time";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new HospitalException("Error fetching appointments by date: " + e.getMessage(), e);
        }
        return list;
    }

    public boolean updateStatus(int id, Appointment.Status status) throws HospitalException {
        String sql = "UPDATE appointments SET status = ? WHERE appointment_id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt   (2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new HospitalException("Error updating status: " + e.getMessage(), e);
        }
    }

    private Appointment map(ResultSet rs) throws SQLException {
        Appointment a = new Appointment();
        a.setAppointmentId  (rs.getInt   ("appointment_id"));
        a.setPatientId      (rs.getInt   ("patient_id"));
        a.setDoctorId       (rs.getInt   ("doctor_id"));
        a.setAppointmentDate(rs.getDate  ("appointment_date").toLocalDate());
        a.setAppointmentTime(rs.getTime  ("appointment_time").toLocalTime());
        a.setReason         (rs.getString("reason"));
        a.setStatus         (Appointment.Status.valueOf(rs.getString("status")));
        a.setNotes          (rs.getString("notes"));
        a.setFeeCharged     (rs.getDouble("fee_charged"));
        a.setPatientName    (rs.getString("patient_name"));
        a.setDoctorName     (rs.getString("doctor_name"));
        a.setDoctorSpecialization(rs.getString("specialization"));
        return a;
    }
}
