package exception;

import api.StatusCode;

public class UserNameMismatch extends ApplicationException{

    public UserNameMismatch(String message) {
        super(StatusCode.FORBIDDEN.getCode(), message);
    }
}
