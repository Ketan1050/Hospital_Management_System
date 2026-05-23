package hospital.service;

import hospital.dao.DoctorDAO;
import hospital.exception.HospitalException;
import hospital.exception.RecordNotFoundException;
import hospital.model.Doctor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DoctorService {

    private final DoctorDAO dao = new DoctorDAO();

    public Doctor addDoctor(Doctor d) throws HospitalException {
        validate(d);
        return dao.save(d);
    }

    public Doctor getDoctorById(int id) throws HospitalException {
        return dao.findById(id)
                  .orElseThrow(() -> new RecordNotFoundException("Doctor", id));
    }

    public List<Doctor> getAllDoctors() throws HospitalException {
        return dao.findAll();
    }

    public List<Doctor> getDoctorsBySpecialization(String spec) throws HospitalException {
        return dao.findBySpecialization(spec);
    }

    public boolean updateDoctor(Doctor d) throws HospitalException {
        validate(d);
        return dao.update(d);
    }

    public boolean deleteDoctor(int id) throws HospitalException {
        return dao.delete(id);
    }

    /** Advanced Java: Streams — group doctors by specialization */
    public Map<String, List<Doctor>> groupBySpecialization() throws HospitalException {
        return dao.findAll().stream()
                  .collect(Collectors.groupingBy(Doctor::getSpecialization));
    }

    /** Advanced Java: Streams — doctors with 10+ years experience */
    public List<Doctor> getSeniorDoctors() throws HospitalException {
        return dao.findAll().stream()
                  .filter(d -> d.getExperienceYears() >= 10)
                  .sorted((a, b) -> b.getExperienceYears() - a.getExperienceYears())
                  .collect(Collectors.toList());
    }

    private void validate(Doctor d) throws HospitalException {
        if (d.getName() == null || d.getName().isBlank())
            throw new HospitalException("Doctor name cannot be empty.");
        if (d.getSpecialization() == null || d.getSpecialization().isBlank())
            throw new HospitalException("Specialization cannot be empty.");
        if (d.getConsultationFee() < 0)
            throw new HospitalException("Consultation fee cannot be negative.");
    }
}
