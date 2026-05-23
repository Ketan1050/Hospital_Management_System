package hospital.service;

import hospital.dao.PatientDAO;
import hospital.exception.HospitalException;
import hospital.exception.RecordNotFoundException;
import hospital.model.Patient;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PatientService {

    private final PatientDAO dao = new PatientDAO();

    public Patient addPatient(Patient p) throws HospitalException {
        validatePatient(p);
        return dao.save(p);
    }

    public Patient getPatientById(int id) throws HospitalException {
        return dao.findById(id)
                  .orElseThrow(() -> new RecordNotFoundException("Patient", id));
    }

    public List<Patient> getAllPatients() throws HospitalException {
        return dao.findAll();
    }

    public List<Patient> searchByName(String name) throws HospitalException {
        return dao.searchByName(name);
    }

    public boolean updatePatient(Patient p) throws HospitalException {
        validatePatient(p);
        return dao.update(p);
    }

    public boolean deletePatient(int id) throws HospitalException {
        return dao.delete(id);
    }

    /** Advanced Java: Streams — group patients by blood group */
    public Map<String, List<Patient>> groupByBloodGroup() throws HospitalException {
        return dao.findAll().stream()
                  .collect(Collectors.groupingBy(
                      p -> p.getBloodGroup() == null ? "Unknown" : p.getBloodGroup()
                  ));
    }

    /** Advanced Java: Streams — sort patients by age */
    public List<Patient> getPatientsSortedByAge() throws HospitalException {
        return dao.findAll().stream()
                  .sorted(Comparator.comparingInt(Patient::getAge))
                  .collect(Collectors.toList());
    }

    private void validatePatient(Patient p) throws HospitalException {
        if (p.getName() == null || p.getName().isBlank())
            throw new HospitalException("Patient name cannot be empty.");
        if (p.getAge() <= 0 || p.getAge() > 150)
            throw new HospitalException("Invalid patient age: " + p.getAge());
        if (!p.getGender().matches("(?i)Male|Female|Other"))
            throw new HospitalException("Gender must be Male, Female, or Other.");
    }
}
