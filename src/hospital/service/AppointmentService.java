package hospital.service;

import hospital.dao.AppointmentDAO;
import hospital.exception.HospitalException;
import hospital.exception.RecordNotFoundException;
import hospital.model.Appointment;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AppointmentService {

    private final AppointmentDAO dao = new AppointmentDAO();

    public Appointment bookAppointment(Appointment a) throws HospitalException {
        validate(a);
        return dao.save(a);
    }

    public Appointment getAppointmentById(int id) throws HospitalException {
        return dao.findById(id)
                  .orElseThrow(() -> new RecordNotFoundException("Appointment", id));
    }

    public List<Appointment> getAllAppointments() throws HospitalException {
        return dao.findAll();
    }

    public List<Appointment> getAppointmentsByPatient(int patientId) throws HospitalException {
        return dao.findByPatient(patientId);
    }

    public List<Appointment> getAppointmentsByDoctor(int doctorId) throws HospitalException {
        return dao.findByDoctor(doctorId);
    }

    public List<Appointment> getAppointmentsByDate(LocalDate date) throws HospitalException {
        return dao.findByDate(date);
    }

    public boolean updateAppointment(Appointment a) throws HospitalException {
        validate(a);
        return dao.update(a);
    }

    public boolean cancelAppointment(int id) throws HospitalException {
        return dao.updateStatus(id, Appointment.Status.CANCELLED);
    }

    public boolean completeAppointment(int id) throws HospitalException {
        return dao.updateStatus(id, Appointment.Status.COMPLETED);
    }

    public boolean deleteAppointment(int id) throws HospitalException {
        return dao.delete(id);
    }

    /** Advanced Java: Streams — group by status */
    public Map<Appointment.Status, List<Appointment>> groupByStatus() throws HospitalException {
        return dao.findAll().stream()
                  .collect(Collectors.groupingBy(Appointment::getStatus));
    }

    /** Advanced Java: Streams — today's appointments */
    public List<Appointment> getTodaysAppointments() throws HospitalException {
        LocalDate today = LocalDate.now();
        return dao.findAll().stream()
                  .filter(a -> a.getAppointmentDate().isEqual(today))
                  .collect(Collectors.toList());
    }

    /** Total revenue from completed appointments */
    public double getTotalRevenue() throws HospitalException {
        return dao.findAll().stream()
                  .filter(a -> a.getStatus() == Appointment.Status.COMPLETED)
                  .mapToDouble(Appointment::getFeeCharged)
                  .sum();
    }

    private void validate(Appointment a) throws HospitalException {
        if (a.getAppointmentDate() == null)
            throw new HospitalException("Appointment date cannot be null.");
        if (a.getAppointmentTime() == null)
            throw new HospitalException("Appointment time cannot be null.");
        if (a.getPatientId() <= 0 || a.getDoctorId() <= 0)
            throw new HospitalException("Valid Patient ID and Doctor ID are required.");
    }
}
