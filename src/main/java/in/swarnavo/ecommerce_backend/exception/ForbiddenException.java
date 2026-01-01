package in.swarnavo.ecommerce_backend.exception;

public class ForbiddenException extends BaseException {

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException() {
        super("You don't have permission to access this resource");
    }
}
