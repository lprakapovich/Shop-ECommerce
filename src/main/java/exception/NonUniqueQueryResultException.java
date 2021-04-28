package exception;

import api.StatusCode;

public class NonUniqueQueryResultException extends ApplicationException {

    public NonUniqueQueryResultException(String message) {
        super(StatusCode.BAD_REQUEST.getCode(), message);
    }
}
