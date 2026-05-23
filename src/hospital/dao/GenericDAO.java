package hospital.dao;

import hospital.exception.HospitalException;
import java.util.List;
import java.util.Optional;


public interface GenericDAO<T, ID> {

    T save(T entity) throws HospitalException;

    Optional<T> findById(ID id) throws HospitalException;

    List<T> findAll() throws HospitalException;

    boolean update(T entity) throws HospitalException;

    boolean delete(ID id) throws HospitalException;
}
