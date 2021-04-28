package exception;

import api.StatusCode;

public class BadRequestException extends ApplicationException {

    public BadRequestException(String message) {
        super(StatusCode.BAD_REQUEST.getCode(), message);
    }
}
