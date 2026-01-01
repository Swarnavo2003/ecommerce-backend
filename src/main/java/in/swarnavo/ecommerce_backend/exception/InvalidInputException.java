package in.swarnavo.ecommerce_backend.exception;

public class InvalidInputException extends BaseException{

    public InvalidInputException(String field, String reason) {
        super(String.format("Invalid input for field '%s': %s", field, reason));
    }

    public InvalidInputException(String message) {
        super(message);
    }
}
