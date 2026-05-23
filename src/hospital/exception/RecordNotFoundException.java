package hospital.exception;

public class RecordNotFoundException extends HospitalException {
    public RecordNotFoundException(String entity, int id) {
        super(entity + " with ID " + id + " not found.", 404);
    }
}
