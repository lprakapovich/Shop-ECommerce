package exception;

public class BadRequestException extends ApplicationException {

    public BadRequestException(int code, String message) {
        super(code, message);
    }
}
