package in.swarnavo.ecommerce_backend.exception;

public class UnauthorizedException extends BaseException{

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException() {
        super("You must be logged in to access this resource");
    }
}
