package handler;

import api.Handler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import exception.GlobalExceptionHandler;
import service.UserService;

public class RegistrationHandler extends Handler {

    private final UserService userService;

    public RegistrationHandler(ObjectMapper objectMapper, GlobalExceptionHandler handler, UserService userService) {
        super(objectMapper, handler);
        this.userService = userService;
    }

    @Override
    protected void execute(HttpExchange exchange) {

    }
}
