package handler;

import api.HeaderDecoder;
import api.Response;
import api.StatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import exception.GlobalExceptionHandler;
import model.user.User;
import service.UserService;

import java.io.IOException;

public class LoginHandler extends Handler {

    private final UserService userService;

    public LoginHandler(ObjectMapper objectMapper, GlobalExceptionHandler exceptionHandler, UserService userService) {
        super(objectMapper, exceptionHandler);
        this.userService = userService;
    }

    protected byte[] resolveRequest(HttpExchange exchange) throws IOException {
        Response<User> loginResponse = handleLogin(exchange);
        return getResponseBodyAsBytes(loginResponse, exchange);
    }

    private Response<User> handleLogin(HttpExchange exchange) {
        String email = HeaderDecoder.decryptHeaderUsername(exchange);
        String password = HeaderDecoder.decryptHeaderPassword(exchange);
        User authenticatedUser = userService.authenticate(email, password);
        return Response.<User>builder()
                .body(authenticatedUser)
                .headers(getHeaders())
                .status(StatusCode.OK)
                .build();
    }
}
